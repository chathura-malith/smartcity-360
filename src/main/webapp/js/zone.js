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
    loadZoneHierarchy();

    document.getElementById('zoneForm').addEventListener('submit',async(e) => {
        e.preventDefault();

        const parentVal = document.getElementById('parentZoneId').value;
        const requestData = {
            name: document.getElementById('zoneName').value,
            type: document.getElementById('zoneType').value,
            parentId: parentVal ? parseInt(parentVal) : null,
            powerStatus: document.getElementById('powerStatus').value,
            energyConsumption: parseFloat(document.getElementById('initialEnergy').value || 0.0)
        };

        try {
            const response = await fetch(`${API_BASE_URL}/zones`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData)
            });

            const result = await response.json();
            if (response.status === 201) {
                Toast.fire({ icon: 'success', title: result.message });
                document.getElementById('zoneForm').reset();
                loadZoneHierarchy();
            } else {
                Toast.fire({ icon: 'error', title: result.message || 'Error creating zone' });
            }
        } catch (error) {
            console.error('Error creating zone:', error);
            Toast.fire({ icon: 'error', title: 'Failed to connect to server!' });
        }
    });
});

async function loadZoneHierarchy() {
    const tableBody = document.getElementById('zoneTableBody');
    const parentSelect = document.getElementById('parentZoneId');

    tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4">
<div class="spinner-border spinner-border-sm text-primary me-2"></div>Loading zones...</td></tr>`;

    try {
        const response = await fetch(`${API_BASE_URL}/zones`);
        const result = await response.json();

        if (response.status === 200) {
            tableBody.innerHTML = '';
            parentSelect.innerHTML = '<option value="" selected>None (Root/City Level)</option>';

            if (!result.data || result.data.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-4 text-muted">
No zones found in database.</td></tr>`;
                return;
            }

            renderZoneTree(result.data, tableBody, parentSelect, 0);
        } else {
            tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-3 text-danger fw-bold">
Error loading hierarchy</td></tr>`;
        }
    } catch (error) {
        console.error('Error fetching zones:', error);
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center py-3 text-danger fw-bold">
Failed to fetch data from GlassFish server!</td></tr>`;
    }
}

function renderZoneTree(zones, tableBody, parentSelect, level) {
    if (!zones) return;

    zones.forEach(zone => {
        const indent = '&nbsp;'.repeat(level * 8);
        const isLeaf = zone.type === 'BUILDING';

        let icon = '<i class="fa-solid fa-city text-primary"></i>';
        if (zone.type === 'DISTRICT') icon = '<i class="fa-solid fa-map text-success"></i>';
        if (zone.type === 'STREET') icon = '<i class="fa-solid fa-road text-warning"></i>';
        if (zone.type === 'BUILDING') icon = '<i class="fa-solid fa-building text-secondary"></i>';

        const powerBadge = zone.powerStatus === 'ON'
            ? '<span class="badge bg-success shadow-sm">ON</span>'
            : '<span class="badge bg-danger shadow-sm">OFF</span>';

        const isPowerOn = zone.powerStatus === 'ON';
        const actionBtnClass = isPowerOn ? 'btn-danger' : 'btn-success';
        const actionBtnIcon = isPowerOn ? 'fa-power-off' : 'fa-bolt';
        const actionBtnText = isPowerOn ? 'Shutdown Area' : 'Turn ON';

        const row = `
            <tr>
                <td class="text-muted fw-bold">#${zone.id}</td>
                <td class="fw-semibold">
                    ${indent} ${icon} <span class="ms-1">${zone.name}</span>
                    <span class="badge bg-light text-dark border ms-2" style="font-size:0.7em">${zone.type}</span>
                </td>
                <td id="status-cell-${zone.id}">${powerBadge}</td>
                <td class="fw-bold text-primary" id="usage-cell-${zone.id}">
                    <button class="btn btn-sm btn-outline-primary py-0 px-2" onclick="calculateUsage(${zone.id})">
                        <i class="fa-solid fa-calculator"></i> Get Total
                    </button>
                </td>
                <td class="text-center">
                    <button class="btn btn-sm ${actionBtnClass} shadow-sm" 
                            onclick="triggerPowerToggle(${zone.id}, '${zone.name}', ${isPowerOn})">
                        <i class="fa-solid ${actionBtnIcon} me-1"></i> ${actionBtnText}
                    </button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;

        if (!isLeaf) {
            const prefix = '- '.repeat(level);
            parentSelect.innerHTML += `<option value="${zone.id}">${prefix}${zone.name} (${zone.type})</option>`;
        }

        if (zone.subZones && zone.subZones.length > 0) {
            renderZoneTree(zone.subZones, tableBody, parentSelect, level + 1);
        }
    });
}

async function triggerPowerToggle(zoneId, zoneName, isPowerOn) {
    const actionText = isPowerOn ? 'SHUTDOWN' : 'TURN ON';

    const confirmResult = await Swal.fire({
        title: `${actionText} Power?`,
        text: `Are you sure you want to ${actionText.toLowerCase()} power for [${zoneName}] and all its sub-zones?`,
        icon: isPowerOn ? 'warning' : 'question',
        showCancelButton: true,
        confirmButtonColor: isPowerOn ? '#d33' : '#198754',
        cancelButtonColor: '#6c757d',
        confirmButtonText: `Yes, ${actionText}`
    });

    if (!confirmResult.isConfirmed) return;

    try {
        const response = await fetch(`${API_BASE_URL}/zones/${zoneId}/shutdown`, { method: 'POST' });
        const result = await response.json();

        if (response.ok) {
            Toast.fire({ icon: 'success', title: result.message || 'Power status updated!' });
            loadZoneHierarchy();
        } else {
            Toast.fire({ icon: 'error', title: result.message || 'Operation failed' });
        }
    } catch (error) {
        Toast.fire({ icon: 'error', title: 'Server connection failed!' });
    }
}

async function calculateUsage(zoneId) {
    const cell = document.getElementById(`usage-cell-${zoneId}`);
    cell.innerHTML = '<span class="spinner-border spinner-border-sm text-primary"></span>';

    try {
        const response = await fetch(`${API_BASE_URL}/zones/${zoneId}/usage`);
        const result = await response.json();

        if (response.ok && result.data !== undefined && result.data !== null) {
            const val = parseFloat(result.data);
            cell.innerHTML = `<span class="badge bg-primary fs-6">${val.toFixed(2)} kWh</span>`;
            Toast.fire({ icon: 'info', title: `Usage calculated for Zone #${zoneId}` });
        } else {
            cell.innerHTML = '<span class="badge bg-secondary fs-6">0.00 kWh</span>';
        }
    } catch (error) {
        cell.innerHTML = '<span class="text-danger fw-bold">Error</span>';
    }
}