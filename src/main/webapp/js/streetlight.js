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
    loadStreetlights();

    document.getElementById('streetlightForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const requestData = {
            modelName: document.getElementById('modelName').value.trim(),
            wattage: parseInt(document.getElementById('wattage').value),
            colorTemperature: document.getElementById('colorTemp').value,
            latitude: parseFloat(document.getElementById('latitude').value),
            longitude: parseFloat(document.getElementById('longitude').value)
        };

        try {
            const response = await fetch(`${API_BASE_URL}/streetlights`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();
            if (response.status === 201) {
                Toast.fire({ icon: 'success', title: result.message });
                document.getElementById('streetlightForm').reset();
                loadStreetlights();
            } else {
                Toast.fire({ icon: 'error', title: result.message || 'Error adding streetlight' });
            }
        } catch (error) {
            console.error('Error submitting form:', error);
            Toast.fire({ icon: 'error', title: 'Failed to connect to server!' });
        }
    });
});

async function loadStreetlights() {
    const tableBody = document.getElementById('streetlightTableBody');
    tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4">
<div class="spinner-border spinner-border-sm text-primary me-2"></div>Loading streetlights...</td></tr>`;

    try {
        const response = await fetch(`${API_BASE_URL}/streetlights`);
        const result = await response.json();

        if (response.status === 200) {
            tableBody.innerHTML = '';

            if (!result.data || result.data.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4 text-muted">
No streetlights deployed yet.</td></tr>`;
                return;
            }

            result.data.forEach(light => {
                const statusBadge = light.status === 'ON'
                    ? '<span class="badge bg-success shadow-sm"><i class="fa-solid fa-power-off me-1"></i>ON</span>'
                    : '<span class="badge bg-danger shadow-sm"><i class="fa-solid fa-power-off me-1"></i>OFF</span>';

                const row = `
                    <tr>
                        <td class="fw-bold text-muted">#${light.id}</td>
                        <td class="fw-bold text-primary"><i class="fa-solid fa-microchip me-1"></i> ${light.modelName}</td>
                        <td class="fw-semibold">
                            <span class="badge bg-light text-dark border me-1">${light.wattage}W</span>
                            <span class="badge bg-light text-dark border">${light.colorTemperature}</span>
                        </td>
                        <td class="font-monospace text-secondary">
                            <i class="fa-solid fa-location-dot text-danger me-1">
                            
</i> ${light.latitude.toFixed(6)}, ${light.longitude.toFixed(6)}
                        </td>
                        <td class="text-center">${statusBadge}</td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        }
    } catch (error) {
        console.error('Error fetching streetlights:', error);
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-3 text-danger fw-bold">
Failed to fetch data from GlassFish server!</td></tr>`;
    }
}