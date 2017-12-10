function fetchComponent(name, doc) {
    if (name == "SSH") {
        var pe = doc.createElement('ProcessingElement');
        pe.setAttribute('name', 'SSH Processing element');
        pe.setAttribute('Type', 'PROCESSING_ELEMENT');
        pe.setAttribute('Compute-host', '192.168.1.112');
        pe.setAttribute('User', 'root');
        pe.setAttribute('Password', 'password');
        pe.setAttribute('Command', '');
        pe.setAttribute('Arguments', '');
        pe.setAttribute("out-1", "Output");
        pe.setAttribute("out-2", "Error");
        pe.setAttribute("in-1", "Input");
        return pe;
    } else if (name == "CP") {
        var pe = doc.createElement('ProcessingElement');
        pe.setAttribute('name', 'Copy Processing element');
        pe.setAttribute('type', 'PROCESSING_ELEMENT');
        pe.setAttribute("out-1", "Output");
        pe.setAttribute("out-2", "Error");
        pe.setAttribute("in-1", "Input");
        return pe;
    } else if (name == "S3") {
        var pe = doc.createElement('ProcessingElement');
        pe.setAttribute('name', 'S3 Processing element');
        pe.setAttribute('type', 'PROCESSING_ELEMENT');
        pe.setAttribute("out-1", "Output");
        pe.setAttribute("out-2", "Error");
        pe.setAttribute("in-1", "Input");
        return pe;
    } else if (name == "START") {
        var pe = doc.createElement('ProcessingElement');
        pe.setAttribute('name', 'Start Operation');
        pe.setAttribute('type', 'OPERATION');
        pe.setAttribute("out-1", "Output");
        return pe;
    } else if (name == "STOP") {
        var pe = doc.createElement('ProcessingElement');
        pe.setAttribute('name', 'Stop Operation');
        pe.setAttribute('type', 'OPERATION');
        pe.setAttribute("in-1", "Input");
        return pe;
    } else if (name = "PARALLEL") {
        var pe = doc.createElement('ProcessingElement');
        pe.setAttribute('name', 'Parallel Operation');
        pe.setAttribute('type', 'OPERATION');
        pe.setAttribute("out-1", "Output1");
        pe.setAttribute("out-2", "Output2");
        pe.setAttribute("in-1", "Input");
        return pe;
    }

}