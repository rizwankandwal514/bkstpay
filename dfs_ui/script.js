const API_KEY = 'testing';

function handleError(response) {
    if (!response.ok) {
        return response.json().then(err => { throw new Error(err.message) });
    }
    return response.json();
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    errorDiv.textContent = message;
}

function clearError() {
    showError('');
}

function uploadFile() {
    clearError();
    const fileInput = document.getElementById('fileUpload');
    const file = fileInput.files[0];

    if (!file) {
        showError('Please select a file to upload.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    fetch('http://localhost:8080/api/v1/dfs/upload', {
        method: 'POST',
        headers: { 'API_KEY': API_KEY },
        body: formData
    })
    .then(handleError)
    .then(data => alert(data.message))
    .catch(error => showError('Upload Failed: ' + error.message));
}

function retrieveContent() {
    clearError();
    const fileName = document.getElementById('fileName').value;
    if (!fileName) {
        showError('Please enter a file name.');
        return;
    }

    fetch(`http://localhost:8080/api/v1/dfs/retrieve-content/${fileName}`, {
        method: 'GET',
        headers: { 'API_KEY': API_KEY }
    })
    .then(handleError)
    .then(data => {
        document.getElementById('fileContent').value = data.data;
    })
    .catch(error => showError('Retrieval Failed: ' + error.message));
}

function updateContent() {
    clearError();
    const fileName = document.getElementById('updateFileName').value;
    const fileContent = document.getElementById('updateFileContent').value;

    if (!fileName || !fileContent) {
        showError('Please enter both file name and new content.');
        return;
    }

    fetch('http://localhost:8080/api/v1/dfs/update-file', {
        method: 'POST',
        headers: { 
            'API_KEY': API_KEY,
            'Content-Type': 'application/json' 
        },
        body: JSON.stringify({ fileName, message: fileContent })
    })
    .then(handleError)
    .then(data => alert(data.message))
    .catch(error => showError('Update Failed: ' + error.message));
}
