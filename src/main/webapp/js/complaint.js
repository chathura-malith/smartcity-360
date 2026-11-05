const API_BASE_URL = 'http://localhost:8080/smartcity-360/api/v1';

const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadComplaints();

    document.getElementById('complaintForm').addEventListener(
        'submit', async (e) => {
        e.preventDefault();

        const requestData = {
            description: document.getElementById('complaintDescription').value,
            severity: document.getElementById('complaintSeverity').value
        };

        try {
            const response = await fetch(`${API_BASE_URL}/complaints`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();
            if (response.status === 201) {
                Toast.fire({ icon: 'success', title: result.message || 'Complaint Processed!' });
                document.getElementById('complaintForm').reset();
                loadComplaints();
            } else {
                Toast.fire({ icon: 'error', title: result.message || 'Error processing complaint' });
            }
        } catch (error) {
            console.error('Error submitting complaint:', error);
            Toast.fire({ icon: 'error', title: 'Failed to connect to server!' });
        }
    });
});

async function loadComplaints() {
    const tableBody = document.getElementById('complaintTableBody');
    tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4"><div class="spinner-border
 spinner-border-sm text-primary me-2"></div>Loading complaints...</td></tr>`;

    try {
        const response = await fetch(`${API_BASE_URL}/complaints`);
        const result = await response.json();

        if (response.status === 200) {
            tableBody.innerHTML = '';

            if (!result.data || result.data.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4 text-muted">
No complaints logged in system.</td></tr>`;
                return;
            }

            result.data.forEach(complaint => {
                let severityBadge = '<span class="badge bg-info">LOW</span>';
                if (complaint.severity === 'MEDIUM') severityBadge = '' +
                    '<span class="badge bg-warning text-dark">MEDIUM</span>';
                if (complaint.severity === 'HIGH') severityBadge = '' +
                    '<span class="badge bg-danger">HIGH</span>';

                let statusBadge = '<span class="badge bg-secondary">PENDING</span>';
                if (complaint.status === 'RESOLVED') statusBadge = '' +
                    '<span class="badge bg-success">RESOLVED</span>';
                if (complaint.status === 'ESCALATED') statusBadge = '' +
                    '<span class="badge bg-warning text-dark">ESCALATED</span>';

                let handlerBadge = '<span class="badge bg-primary">Local Staff</span>';
                if (complaint.assignedHandler === 'SUPERVISOR') handlerBadge =
                    '<span class="badge bg-dark">Supervisor</span>';
                if (complaint.assignedHandler === 'CITY_MANAGER') handlerBadge =
                    '<span class="badge bg-danger">City Manager</span>';

                const row = `
                    <tr>
                        <td class="fw-bold text-primary">${complaint.complaintCode}</td>
                        <td class="fw-semibold">${complaint.description}</td>
                        <td>${severityBadge}</td>
                        <td>${statusBadge}</td>
                        <td>${handlerBadge}</td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        }
    } catch (error) {
        console.error('Error fetching complaints:', error);
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-3 text-danger fw-bold">
Failed to fetch data from GlassFish server!</td></tr>`;
    }
}