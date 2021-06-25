/**
 * IIFE function which is executed when the parishes.html is loaded. This function loads all the data needed for the page.
 */
(async () =>
{
    await addDemoData();
    await addMunicipalities();
    const parishList = await getAllParishes();
    await createParishInputForms();
    populateParishTable(parishList);
})()

/**
 * Creates the input forms to create a new Parish, and populates the municipality dropdown with all the existing municipalities
 * obtained from the getAllMunicipalities function.
 * @returns {Promise<void>}
 */
async function createParishInputForms()
{
    const createParishDiv = document.getElementById("createParishDiv");
    createParishDiv.innerHTML +=
        `
            <form onsubmit="createParish()">
                <label>Sogne Kode: </label><input type="number" id="parishCode" required>
                <label>Sogne Navn: </label><input type="text" id="parishName" required>
                <label>Smitte procent: </label><input type="number" min="0" max="100" id="parishInfectionPercent" required>
                <label>Kommune: 
                    <select name="municipality" id="municipality" required>
                        <option value="" disabled selected>Vælg kommune: </option>
                    </select>              
                </label>
                <button class="btn btn-primary" id="createParishButton">Gem</button>
            </form>
        `

    const municipalityDropdown = document.getElementById("municipality");
    const municipalityList = await getAllMunicipalities();

    municipalityList.forEach((municipality,index) => {
        municipalityDropdown.innerHTML +=
            `
                <option value="${index}">${municipality.navn}</option> 
            `
    })
}

/**
 * Populates the Parish table for the main page. It iterates over the parishList twice with a counter, so unique ids for
 * the elements can be created for every button.
 * The checkboxes are dynamically checked, depending on the shutdown date is null or in the future/past.
 * @param parishList
 */
function populateParishTable(parishList)
{
    const parishTableBody = document.getElementById("parishTableBody");

    parishList.forEach((parish, counter) =>
    {
        parishTableBody.innerHTML +=
            `
                <tr id="parishRow${counter}">
                    <th scope="row">${parish.parishId}</th>
                    <td>${parish.parishCode}</td>
                    <td>${parish.parishName}</td>
                    <td>${parish.infectionPercent}%</td>
                    <td id="parishShutdownCheckboxColumn${counter}"></td>
                    <td id="parishShutdownDateColumn${counter}">${parish.shutdownDate}</td>
                    <td>${parish.municipality.municipalityName}</td>
                    <td id="editParish${counter}"></td>
                    <td id="deleteParish${counter}"></td>
                </tr>
            `
        const parishShutdownCheckboxColumn = document.getElementById(`parishShutdownCheckboxColumn${counter}`);
        const parishShutdownDateColumn = document.getElementById(`parishShutdownDateColumn${counter}`);
        const date = new Date(parish.shutdownDate);
        if(parish.shutdownDate === null)
        {
            parishShutdownCheckboxColumn.innerHTML += "<input type='checkbox' disabled>"
            parishShutdownDateColumn.innerText = "Ingen nedluknings dato"
        }
        else if(date > Date.now())
        {
            parishShutdownCheckboxColumn.innerHTML += "<input type='checkbox' disabled>"
        }
        else
        {
            parishShutdownCheckboxColumn.innerHTML += "<input type='checkbox' disabled checked>"
        }
    })

    parishList.forEach((parish, counter) =>
    {
        const editButtonCell = document.getElementById(`editParish${counter}`)
        const deleteButtonCell = document.getElementById(`deleteParish${counter}`)

        const editButton = addEditButton(parish.parishId);
        editButtonCell.appendChild(editButton);

        const deleteButton = addDeleteButton(parish.parishId);
        deleteButtonCell.appendChild(deleteButton);
    })
}

/**
 * Handles the Edit Parish window, which obtains the info of the Parish through an API and populates the input fields
 * with the values obtained.
 * @param parishId
 * @returns {Promise<void>}
 */
async function openParishEditWindow(parishId)
{
    const parishAPI = `/api/getParishById/${parishId}`;
    const parish = await fetch(parishAPI).then(data => data.json());
    const modalHolder = document.getElementById("modalholder");

    modalHolder.style.display = "block";
    modalHolder.innerHTML +=
        `
            <div class="modal-content">
                <div class="container-fluid">
                    <div class="row">
                        <span id="close" class="close"></span>
                        <label>Sogn Kode: </label><input type="number" value="${parish.parishCode}" id="parishCode" class="form-control">
                        <label>Sogn Navn: </label><input type="text" value="${parish.parishName}" id="parishNameUpdate" class="form-control">
                        <label>Sogn Virussmitte: </label><input type="text" value="${parish.infectionPercent}" id="parishInfectionPercent" class="form-control">
                        <label>Sogn Nedlukningsdato: </label><input type="date" value="${parish.shutdownDate}" id="parishShutdownDate" class="form-control">
                        <button id="saveParish" class="btn btn-warning"></button>
                    </div>                
                </div>            
            </div>
        `
    const closeModal = document.getElementById("close");
    const saveParishButton = document.getElementById("saveParish")

    saveParishButton.innerText = "Gem Ændringer";
    saveParishButton.addEventListener("click", () => saveUpdatedParish(parishId))

    closeModal.addEventListener("click",() => {
        modalHolder.style.display = "none"
        location.reload();
    })

    window.onclick = function(event) {
        if (event.target === modalHolder) {
            modalHolder.style.display = "none";
            location.reload();
        }
    }
}

/**
 * Sends an object to the /api/create mapping, where the data is used for a DTO to create a Parish
 * @returns {Promise<void>}
 */
async function createParish()
{
    const parishAPI = "/api/create";
    const postObject = {
            method: 'POST',
            headers: {
                "Content-type": 'application/json',
            },
            body:JSON.stringify({
                "parishCode":document.getElementById("parishCode").value,
                "parishName":document.getElementById("parishName").value,
                "parishInfectionPercent":document.getElementById("parishInfectionPercent").value,
                "municipalityId": document.getElementById("municipality").value + 1
            })
        }

    await fetch(parishAPI,postObject);
}

/**
 * Sends an object to the /api/create mapping, where the data is used for a DTO to update a Parish
 * @param parishId
 * @returns {Promise<void>}
 */
async function saveUpdatedParish(parishId)
{
    const parishAPI = `/api/updateParish`
    const postObject = {
        method: "POST",
        headers: {
            "Content-type": 'application/json',
        },
        body: JSON.stringify({
            "parishId": parishId,
            "parishCode": document.getElementById("parishCode").value,
            "parishName": document.getElementById("parishNameUpdate").value,
            "parishInfectionPercent": document.getElementById("parishInfectionPercent").value,
            "parishShutdownDate": document.getElementById("parishShutdownDate").value
        })
    }

    await fetch(parishAPI,postObject);
    location.reload();
}

/**
 * Sends a parishId to the delete-mapping to be used to delete a Parish from the database
 * @param parishId
 * @returns {Promise<void>}
 */
async function deleteParish(parishId)
{
    const parishAPI = `/api/delete/${parishId}`
    const deleteObject = {
        method:"DELETE"
    }
    await fetch(parishAPI, deleteObject);
}

/**
 * Returns a edit button with an eventlistener implemented, to open the Edit Parish based on the ParishId
 * @param parishId
 * @returns {HTMLButtonElement}
 */
function addEditButton(parishId)
{
    const editButton = document.createElement("button");
    editButton.innerText = "Rediger";
    editButton.classList.add("btn","btn-warning")
    editButton.addEventListener("click", () => openParishEditWindow(parishId))
    return editButton;
}

/**
 * Returns a delete button with an eventlistener implemented, to delete the Parish based on the parishId
 * @param parishId
 * @returns {HTMLButtonElement}
 */
function addDeleteButton(parishId)
{
    const denyButton = document.createElement("button");
    denyButton.innerText = "Slet";
    denyButton.classList.add("btn","btn-danger","float-left");
    denyButton.onclick = async () =>
    {
        if(confirm("Slet dette Sogn?"))
        {
            await deleteParish(parishId);
            location.reload();
        }
    }

    return denyButton;
}

/**
 * Fetches a list of all existing Municipalities from Danmarks Adressers Web API (DAWA)
 * @returns {Promise<any>}
 */
function getAllMunicipalities()
{
    const parishAPI = "https://api.dataforsyningen.dk/kommuner";
    return fetch(parishAPI).then(response => response.json());
}

/**
 * Sends a request to the /api/addDemoData to populate the Database with Demo data.
 * @returns {Promise<void>}
 */
async function addDemoData()
{
    const demoDataButton = document.getElementById("demoData");
    demoDataButton.addEventListener("click", () => {
        const demoDataAPI = "/api/addDemoData"
        fetch(demoDataAPI);
        location.reload();
    })
}

/**
 * Recieves a list of municipalities from the getAllMunicipalities-function and creates a new empty list.
 * It then iterates through the municipality list and retrieves the name of every municipality, which is added as a new object
 * to the new list.
 * The new list is then sent to the addMunicipalities-mapping where the list is added to the municipality table on the database.
 * @returns {Promise<void>}
 */
async function addMunicipalities()
{
    const municipalityAPI = "/api/addMunicipalities";
    const list = await getAllMunicipalities();
    const newList = [];

    list.forEach((municipality) => {
        const municipalityObject = {
            "municipalityName":municipality.navn
        }
        newList.push(municipalityObject)
    })

    const postObject = {
        method: 'POST',
        headers: {
            "Content-type": 'application/json',
        },
        body:JSON.stringify(newList)
    }

    await fetch(municipalityAPI, postObject);
}

/**
 * Returns a list of all parishes from the database
 * @returns {Promise<any>}
 */
function getAllParishes()
{
    const parishAPI = "/api/allParishes";
    return fetch(parishAPI).then(response => response.json());
}