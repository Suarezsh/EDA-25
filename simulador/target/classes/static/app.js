// URL Base de tu API Spring Boot
const API_BASE_URL = 'http://localhost:8080/api'; // Asegúrate de que coincida con tu puerto

// --- Elementos del DOM ---
// Inputs y Botones
const inputInsertarBinario = document.getElementById('inputInsertarBinario');
const btnInsertarBinario = document.getElementById('btnInsertarBinario');
const inputEliminarBinario = document.getElementById('inputEliminarBinario');
const btnEliminarBinario = document.getElementById('btnEliminarBinario');

const inputInsertarAVL = document.getElementById('inputInsertarAVL');
const btnInsertarAVL = document.getElementById('btnInsertarAVL');
const inputEliminarAVL = document.getElementById('inputEliminarAVL');
const btnEliminarAVL = document.getElementById('btnEliminarAVL');

const inputInsertarSplay = document.getElementById('inputInsertarSplay');
const btnInsertarSplay = document.getElementById('btnInsertarSplay');
const inputEliminarSplay = document.getElementById('inputEliminarSplay');
const btnEliminarSplay = document.getElementById('btnEliminarSplay');

const inputInsertarB = document.getElementById('inputInsertarB');
const btnInsertarB = document.getElementById('btnInsertarB');
const inputEliminarB = document.getElementById('inputEliminarB');
const btnEliminarB = document.getElementById('btnEliminarB');

// Áreas de Salida y Explicación
const explanationBinario = document.getElementById('explanationBinario');
const explanationAVL = document.getElementById('explanationAVL');
const explanationSplay = document.getElementById('explanationSplay');
const explanationB = document.getElementById('explanationB');

// Contenedores de Gráficos
const graphContainerBinario = document.getElementById('graphContainerBinario');
const graphContainerAVL = document.getElementById('graphContainerAVL');
const graphContainerSplay = document.getElementById('graphContainerSplay');
const graphContainerB = document.getElementById('graphContainerB');

// Variables para las instancias de vis.js
let networkBinario, networkAVL, networkSplay, networkB;

// --- Funciones Auxiliares ---

// Función genérica para realizar peticiones Fetch
async function fetchApi(url, options = {}) {
    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText || 'Unknown error'}`);
        }
        // Intentar parsear como JSON, si falla, devolver texto
        try {
            return await response.json();
        } catch (e) {
            return await response.text();
        }
    } catch (error) {
        console.error('Error en fetchApi:', error);
        throw error;
    }
}

// Actualiza el texto de un elemento de explicación
function updateExplanation(elementId, message) {
    const element = document.getElementById(elementId);
    element.textContent = message;
    element.classList.remove('loading-message', 'error-message');
}

// Muestra un mensaje de carga
function showLoading(elementId, message = "Procesando...") {
    const element = document.getElementById(elementId);
    element.textContent = message;
    element.classList.add('loading-message');
    element.classList.remove('error-message');
}

// Muestra un mensaje de error
function showError(elementId, message) {
    const element = document.getElementById(elementId);
    element.textContent = message;
    element.classList.add('error-message');
    element.classList.remove('loading-message');
}

// Función para convertir una lista de strings de representación de árbol a nodos y aristas para vis.js
function processTreeData(dataLines, networkInstance, containerId) {
    if (!dataLines || dataLines.length === 0 || typeof dataLines === 'string') {
        if (containerId) {
            const container = document.getElementById(containerId);
            if (container) container.innerHTML = '<p class="text-muted">No hay datos para mostrar o la estructura está vacía.</p>';
        }
        return { nodes: [], edges: [] };
    }

    const nodes = new vis.DataSet();
    const edges = new vis.DataSet();
    const nodeMap = new Map(); // Para mapear el dato del nodo a su ID en vis.js
    let nodeIdCounter = 0;

    // Función recursiva para procesar los datos de forma más estructurada
    // Asume un formato de lista: "- Dato (Izq: datoI, Der: datoD)" o "- Dato"
    // Para Árbol B, el formato es diferente: "- [Clave1, Clave2]"
    // Se requiere una lógica más específica para cada tipo de árbol si el formato es muy distinto.
    // Para este ejemplo, asumiremos un formato común o adaptaremos la lógica.

    // La representación actual del backend es una lista simple.
    // Para árboles (Binario, AVL, Splay) es más fácil de convertir.
    // Árbol B tiene una estructura diferente.

    if (containerId.includes("Binario") || containerId.includes("AVL") || containerId.includes("Splay")) {
        // Lógica para árboles (Binario, AVL, Splay)
        dataLines.forEach((line) => {
            let match = line.match(/^(-*)?\s*-\s*([\w\(\)\s:,-]+)/); // Captura el nodo principal
            if (!match) return;

            const nodeInfo = match[2];
            const indent = match[1] ? match[1].length : 0;

            let nodeId = nodeIdCounter++;
            let nodeLabel = nodeInfo;
            let nodeIdStr = String(nodeId);
            nodeMap.set(nodeLabel.split(' ')[0], nodeIdStr); // Mapear el dato principal al ID

            nodes.add({ id: nodeId, label: nodeLabel, level: indent / 2 }); // Usar nivel para la jerarquía

            // Buscar hijos en líneas subsiguientes
            let currentNodeIndex = dataLines.indexOf(line);
            let currentIndex = currentNodeIndex + 1;
            while (currentIndex < dataLines.length) {
                const nextLine = dataLines[currentIndex];
                const nextIndent = nextLine.match(/^(\s*)-/) ? nextLine.match(/^(\s*)-/)[1].length : 0;

                if (nextIndent > indent) {
                    let childMatch = nextLine.match(/^(-*)?\s*-\s*([\w\(\)\s:,-]+)/);
                    if (!childMatch) { currentIndex++; continue; }
                    const childNodeInfo = childMatch[2];
                    const childLabel = childNodeInfo.split(' ')[0];
                    let childNodeId = nodeIdCounter++;
                    let childNodeIdStr = String(childNodeId);
                    nodeMap.set(childLabel, childNodeIdStr);

                    nodes.add({ id: childNodeId, label: childNodeInfo, level: (nextIndent / 2) + 1 });
                    edges.add({ from: nodeId, to: childNodeId });
                    currentIndex++;
                } else {
                    break; // Terminamos con los hijos de este nodo
                }
            }
        });
    } else if (containerId.includes("B")) {
        // Lógica para Árbol B
        // El backend no proporciona una representación jerárquica fácil para Árbol B.
        // Necesitaríamos una función de traversa y construcción de nodos/bordes más específica.
        // Por ahora, mostraremos los nodos de nivel superior.
        dataLines.forEach((line, index) => {
            let nodeMatch = line.match(/^(-*)?\s*-\s*(\[.*?\])/);
            if (!nodeMatch) return;

            let nodeId = nodeIdCounter++;
            let nodeLabel = nodeMatch[2];
            nodes.add({ id: nodeId, label: nodeLabel, level: index }); // Usar índice como nivel temporal
            nodeMap.set(nodeLabel, String(nodeId)); // Mapear la clave (o lista de claves) al ID
        });
        // Para Árbol B, la visualización de aristas requiere un traversa más profundo y específico.
        // Esta parte requeriría una re-arquitectura de cómo el backend envía los datos del Árbol B.
    }

    // Opciones de visualización para vis.js
    const options = {
        layout: {
            hierarchical: {
                enabled: true,
                direction: 'UD', // Up-Down
                sortMethod: 'directed',
                levelSeparation: 100,
                nodeSpacing: 150,
            },
        },
        nodes: {
            shape: 'box', // 'ellipse', 'circle', 'box', 'database', 'text'
            font: {
                size: 12,
                color: '#333'
            },
            borderWidth: 1,
            shadow: true
        },
        edges: {
            width: 1,
            shadow: true,
            smooth: {
                enabled: true,
                type: "cubicBezier",
                forceDirection: "vertical",
                roundness: 0.4
            }
        },
        physics: {
            enabled: false // Desactivamos la física para usar la disposición jerárquica
        },
        interaction: {
            dragNodes: true,
            dragView: true,
            zoomView: true
        }
    };

    if (containerId) {
        const container = document.getElementById(containerId);
        if (container) {
            // Si ya existe una red en este contenedor, la destruimos antes de crear una nueva
            if (container.networkInstance) {
                container.networkInstance.destroy();
            }
            const newNetwork = new vis.Network(container, { nodes, edges }, options);
            container.networkInstance = newNetwork; // Guardar instancia para futura destrucción

            // Añadir listener para actualizar nodos seleccionados (opcional)
            newNetwork.on("selectNode", function (params) {
                if (params.nodes.length > 0) {
                    const selectedNodeId = params.nodes[0];
                    const nodeData = nodes.get(selectedNodeId);
                    if (nodeData && nodeData.label) {
                        // Aquí podrías llamar al backend para obtener la explicación específica de ese nodo
                        // Por ahora, simplemente mostramos la etiqueta del nodo.
                        const explanationElementId = containerId.replace("graphContainer", "explanation");
                        updateExplanation(explanationElementId, `Detalles del nodo: ${nodeData.label}`);
                    }
                }
            });
        }
    }

    return { nodes, edges };
}

// --- Lógica de los Tabs y Operaciones ---

// Inicializar la primera red (Árbol Binario)
document.addEventListener('DOMContentLoaded', () => {
    initializeNetwork(graphContainerBinario, 'binario');
    initializeNetwork(graphContainerAVL, 'avl');
    initializeNetwork(graphContainerSplay, 'splay');
    initializeNetwork(graphContainerB, 'b');

    // Cargar estado inicial (opcional, si quieres mostrar algo vacío)
    updateExplanation('explanationBinario', 'Selecciona una operación.');
    updateExplanation('explanationAVL', 'Selecciona una operación.');
    updateExplanation('explanationSplay', 'Selecciona una operación.');
    updateExplanation('explanationB', 'Selecciona una operación.');
});

function initializeNetwork(container, type) {
    if (!container) return;
    const nodes = new vis.DataSet();
    const edges = new vis.DataSet();
    const options = {
        layout: {
            hierarchical: {
                enabled: true,
                direction: 'UD',
                sortMethod: 'directed',
                levelSeparation: 100,
                nodeSpacing: 150,
            },
        },
        nodes: { shape: 'box', font: { size: 12, color: '#333' }, borderWidth: 1, shadow: true },
        edges: { width: 1, shadow: true, smooth: { enabled: true, type: "cubicBezier", forceDirection: "vertical", roundness: 0.4 } },
        physics: { enabled: false },
        interaction: { dragNodes: true, dragView: true, zoomView: true }
    };
    const network = new vis.Network(container, { nodes, edges }, options);
    container.networkInstance = network; // Guardar instancia
}


async function handleOperation(
    operationType, // 'insertar' o 'eliminar'
    structureType, // 'binario', 'avl', 'splay', 'b'
    inputElement,
    explanationElementId
) {
    const dato = inputElement.value.trim();
    if (!dato) {
        showError(explanationElementId, `Por favor, ingrese un valor.`);
        return;
    }
    const valor = parseInt(dato, 10);
    if (isNaN(valor)) {
        showError(explanationElementId, `Valor inválido. Ingrese un número.`);
        return;
    }

    showLoading(explanationElementId, `Procesando ${operationType} de ${valor}...`);

    const endpoint = `${API_BASE_URL}/${structureType}/${operationType}`;
    const method = operationType === 'insertar' ? 'POST' : 'DELETE';

    try {
        const response = await fetchApi(endpoint, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(valor),
        });

        updateExplanation(explanationElementId, response); // La respuesta del backend es la explicación
        inputElement.value = ''; // Limpiar input
        await refreshGraph(structureType); // Refrescar gráfico después de la operación
    } catch (error) {
        showError(explanationElementId, `Error: ${error.message}`);
    }
}

async function refreshGraph(structureType) {
    const endpoint = `${API_BASE_URL}/${structureType}/mostrar`;
    const explanationElementId = `explanation${structureType.charAt(0).toUpperCase() + structureType.slice(1)}`;
    const containerId = `graphContainer${structureType.charAt(0).toUpperCase() + structureType.slice(1)}`;

    try {
        const data = await fetchApi(endpoint);
        if (data && typeof data === 'object' && !Array.isArray(data)) { // Si es un objeto de error
            throw new Error(data.message || "Error desconocido al obtener datos.");
        }
        if (Array.isArray(data)) {
            if (data.length === 0) {
                updateExplanation(explanationElementId, `La estructura ${structureType} está vacía.`);
                const container = document.getElementById(containerId);
                 if (container && container.networkInstance) {
                    container.networkInstance.setData({ nodes: new vis.DataSet(), edges: new vis.DataSet() });
                }
            } else {
                 processTreeData(data, null, containerId); // processTreeData se encarga de renderizar
                 updateExplanation(explanationElementId, `Gráfico de ${structureType} actualizado.`);
            }
        } else {
             throw new Error("Formato de datos inesperado recibido del backend.");
        }
    } catch (error) {
        showError(explanationElementId, `Error al actualizar gráfico: ${error.message}`);
        const container = document.getElementById(containerId);
        if (container) container.innerHTML = `<p class="text-danger">Error al cargar gráfico: ${error.message}</p>`;
    }
}

// --- Listeners para Árbol Binario ---
btnInsertarBinario.addEventListener('click', () => handleOperation('insertar', 'arbol-binario', inputInsertarBinario, 'explanationBinario'));
btnEliminarBinario.addEventListener('click', () => handleOperation('eliminar', 'arbol-binario', inputEliminarBinario, 'explanationBinario'));

// --- Listeners para Árbol AVL ---
btnInsertarAVL.addEventListener('click', () => handleOperation('insertar', 'arbol-avl', inputInsertarAVL, 'explanationAVL'));
btnEliminarAVL.addEventListener('click', () => handleOperation('eliminar', 'arbol-avl', inputEliminarAVL, 'explanationAVL'));

// --- Listeners para Splay Tree ---
btnInsertarSplay.addEventListener('click', () => handleOperation('insertar', 'splay-tree', inputInsertarSplay, 'explanationSplay'));
btnEliminarSplay.addEventListener('click', () => handleOperation('eliminar', 'splay-tree', inputEliminarSplay, 'explanationSplay'));

// --- Listeners para Árbol B ---
btnInsertarB.addEventListener('click', () => handleOperation('insertar', 'arbol-b', inputInsertarB, 'explanationB'));
btnEliminarB.addEventListener('click', () => handleOperation('eliminar', 'arbol-b', inputEliminarB, 'explanationB'));

// Inicializar la vista de los árboles al cargar la página (o cuando se muestra el tab)
// Esto se hace llamando a refreshGraph para cada estructura
document.addEventListener('shown.bs.tab', function (event) {
    const targetTabId = event.target.getAttribute('data-bs-target');
    if (targetTabId === '#binario-tab') {
        refreshGraph('arbol-binario');
    } else if (targetTabId === '#avl-tab') {
        refreshGraph('arbol-avl');
    } else if (targetTabId === '#splay-tab') {
        refreshGraph('splay-tree');
    } else if (targetTabId === '#b-tab') {
        refreshGraph('arbol-b');
    }
});

// Llama a refreshGraph para la primera estructura al cargar la página
refreshGraph('arbol-binario');