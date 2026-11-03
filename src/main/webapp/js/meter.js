const API_BASE_URL = 'http://localhost:8080/smartcity-360/api/v1';

const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadZonesDropdown();
    loadMeters();

    document.getElementById('meterForm').addEventListener(
        'submit', async (e) => {
        e.preventDefault();

        const requestData = {
            meterCode: document.getElementById('meterCode').value,
            zoneId: parseInt(document.getElementById('zoneId').value),
            rawReadingKwh: parseFloat(document.getElementById('rawReading').value),
            isEncrypted: document.getElementById('chkEncryption').checked,
            isAuthenticated: document.getElementById('chkAuth').checked,
            isCompressed: document.getElementById('chkCompression').checked
        };

        try {
            const response = await fetch(`${API_BASE_URL}/energy-meters/process`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();
            if (response.status === 201) {
                // Success Toast
                Toast.fire({
                    icon: 'success',
                    title: result.message
                });
                document.getElementById('meterForm').reset();
                loadMeters();
            } else {
                Toast.fire({
                    icon: 'error',
                    title: result.message
                });
            }
        } catch (error) {
            console.error('Error submitting meter data:', error);
            Toast.fire({
                icon: 'error',
                title: 'Failed to connect to GlassFish server!'
            });
        }
    });
});

async function loadZonesDropdown() {
    const zoneSelect = document.getElementById('zoneId');
    try {
        const response = await fetch(`${API_BASE_URL}/zones`);
        if (response.ok) {
            const result = await response.json();
            zoneSelect.innerHTML = '<option value="" selected disabled>Select Zone...</option>';
            result.data.forEach(zone => {
                zoneSelect.innerHTML += `<option value="${zone.id}">${zone.name} (${zone.type})</option>`;
            });
        } else {
            populateFallbackZones();
        }
    } catch (e) {
        populateFallbackZones();
    }
}

function populateFallbackZones() {
    const zoneSelect = document.getElementById('zoneId');
    zoneSelect.innerHTML = `
        <option value="" selected disabled>Select Zone...</option>
        <option value="1">Zone A - Commercial Hub</option>
        <option value="2">Zone B - Industrial Park</option>
        <option value="3">Zone C - Residential Sector 1</option>
    `;
}

async function loadMeters() {
    const tableBody = document.getElementById('meterTableBody');
    tableBody.innerHTML = `
        <tr>
            <td colspan="5" class="text-center py-4 text-muted">
                <div class="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                Loading energy meter streams...
            </td>
        </tr>
    `;

    try {
        const response = await fetch(`${API_BASE_URL}/energy-meters`);
        const result = await response.json();

        if (response.status === 200) {
            tableBody.innerHTML = '';

            if (result.data.length === 0) {
                tableBody.innerHTML = `
                    <tr>
                        <td colspan="5" class="text-center py-4 text-muted">No meter records found in database.</td>
                    </tr>
                `;
                return;
            }

            result.data.forEach(meter => {
                let badgeHTML = '';
                if (meter.isEncrypted) badgeHTML += '<span class="badge bg-danger me-1 shadow-sm">ENC</span>';
                if (meter.isAuthenticated) badgeHTML += '<span class="badge bg-success me-1 shadow-sm">AUTH</span>';
                if (meter.isCompressed) badgeHTML += '<span class="badge bg-warning text-dark me-1 shadow-sm">CMP</span>';
                if (!badgeHTML) badgeHTML = '<span class="badge bg-secondary shadow-sm">RAW</span>';

                const row = `
                    <tr>
                        <td class="fw-bold text-primary">${meter.meterCode}</td>
                        <td><span class="badge bg-info text-dark px-2 py-1 fs-6">${meter.zoneName ||
                'Zone #' + meter.zoneId}</span></td>
                        <td class="fw-semibold">${meter.rawReadingKwh} kWh</td>
                        <td>${badgeHTML}</td>
                        <td>
                            <div class="p-2 bg-light border rounded font-monospace small
                             text-break text-dark text-wrap">
                                ${meter.processedPayload}
                            </div>
                        </td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        }
    } catch (error) {
        console.error('Error fetching meters:', error);
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center py-3 text-danger fw-bold">
                Failed to fetch data from GlassFish server!
                </td>
            </tr>
        `;
    }
}