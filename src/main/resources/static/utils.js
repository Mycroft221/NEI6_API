function getIdent() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('ident');
}

function addIdent(id) {
    let node = document.getElementById(id);
    node.appendChild(document.createTextNode(getIdent()));
}