(async () => {

    await populateMunicipalityInfoTable();
})()


async function populateMunicipalityInfoTable()
{
    const municipalTableDiv = document.getElementById("municipalTableBody");
    const municipalityList = await getMunicipalityInfo();

    municipalityList.forEach((municipality, counter) =>
    {
        municipalTableDiv.innerHTML +=
            `
                <tr>
                    <td scope="row"><b>${municipality.municipalityName}</b></td>
                    <td scope="row"><b>${municipality.totalInfectionPercent.toFixed(1)} %</b></td>
                    <td scope="row"><b>${municipality.parishes.length}</b></td>
                </tr>
                <div id="parishRow${counter}">
                </div>
            `
        const parishRowDiv = document.getElementById(`parishRow${counter}`);
        const parishList = municipality.parishes;

        parishList.forEach(parish => {
            parishRowDiv.innerHTML +=
                `
                    <tr>
                        Sogn: ${parish.parishName} - ${parish.infectionPercent}%<br>
                    </tr>
                `
        })
    })

}

async function getMunicipalityInfo()
{
    const municipalityList = "/api/municipalityInfo"
    return await fetch(municipalityList).then(response => response.json());
}