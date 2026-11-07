const API_BASE_URL = 'http://localhost:8080/smartcity-360/api/v1';

const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3500,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadPolicies();

    // Create New Policy
    document.getElementById('policyForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const requestData = {
            policyName: document.getElementById('policyName').value.trim(),
            ruleExpression: document.getElementById('ruleExpression').value.trim(),
            description: document.getElementById('policyDescription').value.trim()
        };

        try {
            const response = await fetch(`${API_BASE_URL}/policies`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();
            if (response.status === 201) {
                Toast.fire({ icon: 'success', title: result.message });
                document.getElementById('policyForm').reset();
                loadPolicies();
            } else {
                Toast.fire({ icon: 'error', title: result.message || 'Error creating policy' });
            }
        } catch (error) {
            console.error('Error submitting policy:', error);
            Toast.fire({ icon: 'error', title: 'Failed to connect to server!' });
        }
    });

    // Evaluate Existing Policy DSL
    document.getElementById('evaluateForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const policyId = parseInt(document.getElementById('selectPolicy').value);
        const varName = document.getElementById('envVariable').value.trim().toUpperCase();
        const varValue = parseInt(document.getElementById('envValue').value);

        const envData = {};
        envData[varName] = varValue;

        const requestData = {
            policyId: policyId,
            environmentData: envData
        };

        try {
            const response = await fetch(`${API_BASE_URL}/policies/evaluate`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();
            if (response.status === 200) {
                const isTriggered = result.data;
                if (isTriggered) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'POLICY VIOLATION DETECTED!',
                        text: `${result.message} (${varName} = ${varValue})`,
                        confirmButtonColor: '#d33'
                    });
                } else {
                    Swal.fire({
                        icon: 'success',
                        title: 'Policy Normal',
                        text: `${result.message} (${varName} = ${varValue})`,
                        confirmButtonColor: '#198754'
                    });
                }
            } else {
                Toast.fire({ icon: 'error', title: result.message || 'Evaluation failed' });
            }
        } catch (error) {
            console.error('Error evaluating policy:', error);
            Toast.fire({ icon: 'error', title: 'Server connection failed!' });
        }
    });
});

async function loadPolicies() {
    const tableBody = document.getElementById('policyTableBody');
    const selectPolicy = document.getElementById('selectPolicy');
    tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4">
<div class="spinner-border spinner-border-sm text-primary me-2"></div>Loading policies...</td></tr>`;

    try {
        const response = await fetch(`${API_BASE_URL}/policies`);
        const result = await response.json();

        if (response.status === 200) {
            tableBody.innerHTML = '';
            selectPolicy.innerHTML = '<option value="" selected disabled>Choose Policy...</option>';

            if (!result.data || result.data.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4 text-muted">
No policies defined in system.</td></tr>`;
                return;
            }

            result.data.forEach(policy => {
                const statusBadge = policy.isActive
                    ? '<span class="badge bg-success shadow-sm">ACTIVE</span>'
                    : '<span class="badge bg-secondary shadow-sm">INACTIVE</span>';

                const row = `
                    <tr>
                        <td class="fw-bold text-muted">#${policy.id}</td>
                        <td class="fw-bold text-primary">${policy.policyName}</td>
                        <td><code class="fs-6 fw-bold text-dark bg-light px-2 py-1 rounded border">${policy.ruleExpression}</code></td>
                        <td class="text-secondary small">${policy.description || 'N/A'}</td>
                        <td class="text-center">${statusBadge}</td>
                    </tr>
                `;
                tableBody.innerHTML += row;

                // Populate Dropdown for Simulator
                selectPolicy.innerHTML += `<option value="${policy.id}">#${policy.id} - ${policy.policyName}</option>`;
            });
        }
    } catch (error) {
        console.error('Error fetching policies:', error);
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-3 text-danger fw-bold">
Failed to fetch data from GlassFish server!</td></tr>`;
    }
}