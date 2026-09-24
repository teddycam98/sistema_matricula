// Constantes y Estado Global
const API_BASE = "http://localhost:8080/api";

let appState = {
  estudiantes: [],
  periodos: [],
  seccionesDisponibles: [],
  seccionesSeleccionadas: new Map(), // seccionId -> seccion
  matriculas: [],
  cursos: [],
  docentes: [],
  carreras: [],
  planes: []
};

// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  inicializarApp();
});

async function inicializarApp() {
  inicializarTema();
  try {
    await Promise.all([
      cargarDashboard(),
      cargarPeriodos(),
      cargarEstudiantesSelect(),
      cargarCarrerasSelect(),
      cargarPlanesSelect()
    ]);
  } catch (err) {
    console.warn("Error cargando datos iniciales:", err);
  }
}

// ==================== TEMA OSCURO / CLARO ====================
function inicializarTema() {
  const temaGuardado = localStorage.getItem("sistema_matricula_theme");
  if (temaGuardado === "light") {
    aplicarTema(false);
  } else {
    aplicarTema(true); // Por defecto modo oscuro azulino moderno
  }
}

function toggleTheme() {
  const esOscuro = document.body.classList.contains("dark-mode");
  aplicarTema(!esOscuro);
}

function aplicarTema(oscuro) {
  const icon = document.getElementById("theme-toggle-icon");
  const text = document.getElementById("theme-toggle-text");
  if (oscuro) {
    document.body.classList.add("dark-mode");
    localStorage.setItem("sistema_matricula_theme", "dark");
    if (icon) icon.className = "fa-solid fa-sun text-amber-300";
    if (text) text.textContent = "Modo Claro";
  } else {
    document.body.classList.remove("dark-mode");
    localStorage.setItem("sistema_matricula_theme", "light");
    if (icon) icon.className = "fa-solid fa-moon text-amber-300";
    if (text) text.textContent = "Modo Oscuro";
  }
}

// Navegación de Pestañas
function switchTab(tabId) {
  document.querySelectorAll(".tab-content").forEach(el => el.classList.add("hidden"));
  document.querySelectorAll(".nav-btn").forEach(el => el.classList.remove("active-tab"));

  const targetTab = document.getElementById(`tab-${tabId}`);
  const targetNav = document.getElementById(`nav-${tabId}`);

  if (targetTab) targetTab.classList.remove("hidden");
  if (targetNav) targetNav.classList.add("active-tab");

  // Carga según pestaña
  if (tabId === "dashboard") cargarDashboard();
  if (tabId === "matriculas") cargarMatriculas();
  if (tabId === "estudiantes") cargarEstudiantes();
  if (tabId === "cursos") cargarCursos();
  if (tabId === "docentes") cargarDocentes();
  if (tabId === "reportes") cargarModuloReportes();
}

// ==================== DASHBOARD ====================
async function cargarDashboard() {
  try {
    const res = await fetch(`${API_BASE}/dashboard/stats`);
    if (res.ok) {
      const { data } = await res.json();
      document.getElementById("stat-estudiantes").textContent = data.totalEstudiantes || 0;
      document.getElementById("stat-matriculas").textContent = data.totalMatriculas || 0;
      document.getElementById("stat-cursos").textContent = data.totalCursos || 0;
      document.getElementById("stat-secciones").textContent = data.totalSecciones || 0;
      document.getElementById("stat-recaudado").textContent = `S/ ${(data.totalRecaudado || 0).toFixed(2)}`;
    }

    // Cargar últimas matrículas en dashboard
    const matRes = await fetch(`${API_BASE}/matriculas`);
    if (matRes.ok) {
      const { data } = await matRes.json();
      const tbody = document.getElementById("dashboard-matriculas-tbody");
      tbody.innerHTML = "";
      const recientes = data.slice(-5).reverse();

      if (recientes.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="py-4 text-center text-slate-400">No hay matrículas registradas</td></tr>`;
        return;
      }

      recientes.forEach(m => {
        const tr = document.createElement("tr");
        tr.className = "hover:bg-slate-50 transition";
        tr.innerHTML = `
          <td class="py-3 px-3 font-semibold text-indigo-700">${m.codigoMatricula}</td>
          <td class="py-3 px-3 font-medium text-slate-800">${m.estudianteNombreCompleto || "--"}</td>
          <td class="py-3 px-3 text-slate-500">${m.carreraNombre || "--"}</td>
          <td class="py-3 px-3 text-slate-700 font-bold">${m.totalCreditos} cred.</td>
          <td class="py-3 px-3 text-slate-800 font-semibold">S/ ${(m.costoTotal || 0).toFixed(2)}</td>
          <td class="py-3 px-3">${renderBadgeEstado(m.estado)}</td>
        `;
        tbody.appendChild(tr);
      });
    }
  } catch (err) {
    console.error("Error al cargar dashboard:", err);
  }
}

// ==================== MATRÍCULAS ====================
async function cargarMatriculas() {
  try {
    const res = await fetch(`${API_BASE}/matriculas`);
    if (!res.ok) throw new Error("Error en la solicitud");
    const { data } = await res.json();
    appState.matriculas = data;
    renderMatriculasTable(data);
  } catch (err) {
    mostrarToast("No se pudo cargar el listado de matrículas", "error");
  }
}

function renderMatriculasTable(lista) {
  const tbody = document.getElementById("matriculas-tbody");
  tbody.innerHTML = "";

  if (lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="9" class="py-6 text-center text-slate-400">No se encontraron matrículas</td></tr>`;
    return;
  }

  lista.forEach(m => {
    const tr = document.createElement("tr");
    tr.className = "hover:bg-slate-50 transition";
    tr.innerHTML = `
      <td class="py-3 px-4 font-bold text-indigo-700">${m.codigoMatricula}</td>
      <td class="py-3 px-4">
        <p class="font-semibold text-slate-800">${m.estudianteNombreCompleto}</p>
        <p class="text-xs text-slate-400">Cod: ${m.estudianteCodigo}</p>
      </td>
      <td class="py-3 px-4 text-slate-600">${m.estudianteDni}</td>
      <td class="py-3 px-4 text-slate-600">${m.carreraNombre || "--"}</td>
      <td class="py-3 px-4 font-semibold text-slate-700">${m.periodoCodigo}</td>
      <td class="py-3 px-4 text-center font-bold text-indigo-600">${m.totalCreditos}</td>
      <td class="py-3 px-4 font-bold text-slate-800">S/ ${(m.costoTotal || 0).toFixed(2)}</td>
      <td class="py-3 px-4">${renderBadgeEstado(m.estado)}</td>
      <td class="py-3 px-4 text-center space-x-1.5 whitespace-nowrap">
        <button onclick="verDetalleMatricula(${m.id})" title="Ver Cursos" class="p-1.5 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 rounded-md transition">
          <i class="fa-solid fa-eye text-sm"></i>
        </button>
        <button onclick="imprimirPdfMatricula(${m.id})" title="Imprimir Ficha PDF (JasperReports)" class="p-1.5 bg-rose-50 hover:bg-rose-100 text-rose-700 rounded-md transition font-semibold text-xs inline-flex items-center">
          <i class="fa-solid fa-file-pdf text-sm text-rose-600 mr-1"></i> Ficha
        </button>
        ${m.estado === "CONFIRMADA" ? `
          <button onclick="pagarMatricula(${m.id})" title="Registrar Pago" class="p-1.5 bg-emerald-50 hover:bg-emerald-100 text-emerald-600 rounded-md transition">
            <i class="fa-solid fa-cash-register text-sm"></i>
          </button>
        ` : ''}
        ${m.estado !== "ANULADA" ? `
          <button onclick="anularMatricula(${m.id})" title="Anular Matrícula" class="p-1.5 bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-md transition">
            <i class="fa-solid fa-ban text-sm"></i>
          </button>
        ` : ''}
        <button onclick="eliminarMatricula(${m.id}, '${m.codigoMatricula}')" title="Eliminar Matrícula Definitivamente" class="p-1.5 bg-rose-50 hover:bg-rose-100 text-rose-600 rounded-md transition">
          <i class="fa-solid fa-trash text-sm"></i>
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function filtrarMatriculas() {
  const query = document.getElementById("filtro-matriculas").value.toLowerCase().trim();
  const filtradas = appState.matriculas.filter(m =>
    (m.codigoMatricula && m.codigoMatricula.toLowerCase().includes(query)) ||
    (m.estudianteNombreCompleto && m.estudianteNombreCompleto.toLowerCase().includes(query)) ||
    (m.estudianteDni && m.estudianteDni.includes(query)) ||
    (m.estudianteCodigo && m.estudianteCodigo.toLowerCase().includes(query))
  );
  renderMatriculasTable(filtradas);
}

let matriculaModalId = null;

function verDetalleMatricula(id) {
  matriculaModalId = id;
  const m = appState.matriculas.find(x => x.id === id);
  if (!m) return;

  document.getElementById("modal-mat-codigo").textContent = `Matrícula: ${m.codigoMatricula}`;
  document.getElementById("modal-mat-estudiante").textContent = `Alumno: ${m.estudianteNombreCompleto} (${m.carreraNombre || ""})`;
  document.getElementById("modal-mat-dni").textContent = m.estudianteDni;
  document.getElementById("modal-mat-periodo").textContent = m.periodoCodigo;
  document.getElementById("modal-mat-creditos").textContent = `${m.totalCreditos} créditos`;
  document.getElementById("modal-mat-costo").textContent = `S/ ${(m.costoTotal || 0).toFixed(2)}`;

  const tbody = document.getElementById("modal-mat-cursos-tbody");
  tbody.innerHTML = "";

  if (!m.cursos || m.cursos.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" class="py-3 text-center text-slate-400">Sin cursos registrados</td></tr>`;
  } else {
    m.cursos.forEach(c => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="py-2.5 px-3 font-semibold text-indigo-700">${c.cursoCodigo}</td>
        <td class="py-2.5 px-3 font-medium text-slate-800">${c.cursoNombre}</td>
        <td class="py-2.5 px-3 text-slate-600">${c.codigoSeccion}</td>
        <td class="py-2.5 px-3 text-slate-600">${c.docenteNombre || "--"}</td>
        <td class="py-2.5 px-3 font-bold text-slate-800">S/ ${(c.costoCurso || 0).toFixed(2)}</td>
        <td class="py-2.5 px-3"><span class="px-2 py-0.5 rounded text-[11px] font-semibold bg-emerald-100 text-emerald-700">${c.estadoCurso}</span></td>
      `;
      tbody.appendChild(tr);
    });
  }

  document.getElementById("modal-detalle-matricula").classList.remove("hidden");
}

function cerrarModalDetalle() {
  document.getElementById("modal-detalle-matricula").classList.add("hidden");
}

async function eliminarMatricula(id, codigo = "") {
  const cod = codigo || `ID ${id}`;
  if (!confirm(`¿Está seguro de que desea ELIMINAR definitivamente la matrícula ${cod}? Se liberarán las vacantes de los cursos asociados.`)) {
    return;
  }
  try {
    const res = await fetch(`${API_BASE}/matriculas/${id}`, {
      method: "DELETE"
    });
    const data = await res.json();
    if (res.ok && data.success) {
      mostrarToast(`Matrícula ${cod} eliminada exitosamente`, "success");
      cerrarModalDetalle();
      cargarMatriculas();
      cargarDashboard();
    } else {
      mostrarToast(data.message || "Error al eliminar matrícula", "error");
    }
  } catch (err) {
    mostrarToast("Error en la conexión con el servidor", "error");
  }
}

function eliminarMatriculaModal() {
  if (matriculaModalId) {
    const m = appState.matriculas.find(x => x.id === matriculaModalId);
    eliminarMatricula(matriculaModalId, m ? m.codigoMatricula : "");
  }
}

async function anularMatricula(id) {
  if (!confirm("¿Está seguro de que desea anular esta matrícula? Las vacantes serán liberadas.")) {
    return;
  }
  try {
    const res = await fetch(`${API_BASE}/matriculas/${id}/anular`, {
      method: "PATCH"
    });
    const data = await res.json();
    if (res.ok) {
      mostrarToast("Matrícula anulada correctamente", "success");
      cargarMatriculas();
      cargarDashboard();
    } else {
      mostrarToast(data.message || "Error al anular matrícula", "error");
    }
  } catch (err) {
    mostrarToast("Error en la conexión con el servidor", "error");
  }
}

async function pagarMatricula(id) {
  const metodo = prompt("Ingrese método de pago (EFECTIVO, TARJETA, YAPE, TRANSFERENCIA):", "EFECTIVO");
  if (!metodo) return;

  try {
    const res = await fetch(`${API_BASE}/pagos/matricula/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ metodoPago: metodo })
    });
    const data = await res.json();
    if (res.ok) {
      mostrarToast("Pago procesado exitosamente", "success");
      cargarMatriculas();
      cargarDashboard();
    } else {
      mostrarToast(data.message || "Error al procesar pago", "error");
    }
  } catch (err) {
    mostrarToast("Error en la conexión con el servidor", "error");
  }
}

function imprimirPdfMatricula(id) {
  // Abre directamente el endpoint de JasperReports en una nueva pestaña del navegador
  window.open(`${API_BASE}/reportes/matricula/${id}/pdf`, '_blank');
}

function imprimirPdfMatriculaModal() {
  if (matriculaModalId) {
    imprimirPdfMatricula(matriculaModalId);
  }
}

function descargarReporteMatriculasPdf() {
  window.open(`${API_BASE}/reportes/matriculas/pdf`, '_blank');
}

function descargarPadronEstudiantesPdf() {
  window.open(`${API_BASE}/reportes/estudiantes/pdf`, '_blank');
}

// ==================== MÓDULO DE REPORTES JASPERREPORTS ====================
async function cargarModuloReportes() {
  try {
    const res = await fetch(`${API_BASE}/matriculas`);
    if (res.ok) {
      const { data } = await res.json();
      appState.matriculas = data;
      const select = document.getElementById("reporte-select-matricula");
      if (!select) return;
      if (!data || data.length === 0) {
        select.innerHTML = '<option value="">No hay matrículas registradas</option>';
        return;
      }
      select.innerHTML = '';
      data.forEach(m => {
        const opt = document.createElement("option");
        opt.value = m.id;
        const estNombre = m.estudiante ? `${m.estudiante.nombres} ${m.estudiante.apellidos}` : 'Estudiante';
        const carNom = (m.estudiante && m.estudiante.carrera) ? m.estudiante.carrera.nombre : '';
        opt.textContent = `${m.codigoMatricula} - ${estNombre} (${carNom})`;
        select.appendChild(opt);
      });
    }
  } catch (err) {
    console.error("Error al cargar selector de matrículas:", err);
  }
}

function descargarFichaMatriculaSeleccionada() {
  const select = document.getElementById("reporte-select-matricula");
  if (!select || !select.value) {
    mostrarToast("Por favor seleccione una matrícula", "error");
    return;
  }
  imprimirPdfMatricula(select.value);
}

function previsualizarFichaMatriculaSeleccionada() {
  const select = document.getElementById("reporte-select-matricula");
  if (!select || !select.value) {
    mostrarToast("Por favor seleccione una matrícula", "error");
    return;
  }
  const text = select.options[select.selectedIndex]?.text || "Ficha Oficial de Matrícula";
  abrirVisorPdf(`${API_BASE}/reportes/matricula/${select.value}/pdf`, `Constancia Oficial de Matrícula: ${text}`);
}

function previsualizarReporteMatriculasPdf() {
  abrirVisorPdf(`${API_BASE}/reportes/matriculas/pdf`, "Reporte General Consolidado de Matrículas (JasperReports)");
}

function previsualizarPadronEstudiantesPdf() {
  abrirVisorPdf(`${API_BASE}/reportes/estudiantes/pdf`, "Padrón Oficial de Estudiantes Universitarios (JasperReports)");
}

function abrirVisorPdf(url, titulo) {
  const modal = document.getElementById("modal-visor-pdf");
  const iframe = document.getElementById("modal-visor-iframe");
  const tituloEl = document.getElementById("modal-visor-titulo");
  const btnDescargar = document.getElementById("modal-visor-btn-descargar");

  if (tituloEl) tituloEl.textContent = titulo;
  if (btnDescargar) btnDescargar.href = url;
  if (iframe) iframe.src = url;
  if (modal) modal.classList.remove("hidden");
}

function cerrarVisorPdf() {
  const modal = document.getElementById("modal-visor-pdf");
  const iframe = document.getElementById("modal-visor-iframe");
  if (modal) modal.classList.add("hidden");
  if (iframe) iframe.src = "";
}

// ==================== NUEVA MATRÍCULA ====================
async function cargarPeriodos() {
  try {
    const res = await fetch(`${API_BASE}/periodos`);
    if (res.ok) {
      const { data } = await res.json();
      appState.periodos = data;
      const select = document.getElementById("select-periodo");
      select.innerHTML = '<option value="">Seleccione periodo...</option>';
      data.forEach(p => {
        const opt = document.createElement("option");
        opt.value = p.id;
        opt.textContent = `${p.codigo} (${p.estado})`;
        if (p.estado === "ACTIVO") opt.selected = true;
        select.appendChild(opt);
      });
      cargarSeccionesDisponibles();
    }
  } catch (err) {
    console.error("Error al cargar periodos:", err);
  }
}

async function cargarEstudiantesSelect() {
  try {
    const res = await fetch(`${API_BASE}/estudiantes`);
    if (res.ok) {
      const { data } = await res.json();
      appState.estudiantes = data;
      const select = document.getElementById("select-estudiante");
      select.innerHTML = '<option value="">Seleccione un estudiante...</option>';
      data.forEach(e => {
        const opt = document.createElement("option");
        opt.value = e.id;
        opt.textContent = `${e.dni} - ${e.nombres} ${e.apellidos} (${e.carrera ? e.carrera.nombre : ''})`;
        select.appendChild(opt);
      });
    }
  } catch (err) {
    console.error("Error al cargar estudiantes:", err);
  }
}

function actualizarInfoEstudiante() {
  const estId = Number(document.getElementById("select-estudiante").value);
  const infoBanner = document.getElementById("estudiante-info-banner");

  if (!estId) {
    infoBanner.classList.add("hidden");
    return;
  }

  const est = appState.estudiantes.find(x => x.id === estId);
  if (est) {
    document.getElementById("info-dni").textContent = est.dni;
    document.getElementById("info-codigo").textContent = est.codigoEstudiante;
    document.getElementById("info-carrera").textContent = est.carrera ? est.carrera.nombre : "No asignada";
    infoBanner.classList.remove("hidden");
  }
}

async function cargarSeccionesDisponibles() {
  const periodoId = document.getElementById("select-periodo").value;
  const contenedor = document.getElementById("contenedor-secciones");
  appState.seccionesSeleccionadas.clear();
  actualizarResumenMatricula();

  if (!periodoId) {
    contenedor.innerHTML = '<p class="text-sm text-slate-400 text-center py-6">Seleccione un periodo para cargar las secciones.</p>';
    return;
  }

  contenedor.innerHTML = '<p class="text-sm text-slate-400 text-center py-6"><i class="fa-solid fa-spinner fa-spin mr-2"></i> Cargando secciones...</p>';

  try {
    const res = await fetch(`${API_BASE}/secciones/disponibles?periodoId=${periodoId}`);
    if (res.ok) {
      const { data } = await res.json();
      appState.seccionesDisponibles = data;

      if (data.length === 0) {
        contenedor.innerHTML = '<p class="text-sm text-slate-400 text-center py-6">No hay secciones con vacantes disponibles en este periodo.</p>';
        return;
      }

      contenedor.innerHTML = "";
      data.forEach(sec => {
        const item = document.createElement("div");
        item.className = "flex items-center justify-between p-3.5 border border-slate-200 rounded-xl hover:border-indigo-400 transition bg-slate-50/50";
        const vacantesLibres = sec.vacantes - sec.matriculados;

        item.innerHTML = `
          <div class="flex items-center space-x-3">
            <input type="checkbox" id="sec-check-${sec.id}" onchange="toggleSeleccionSeccion(${sec.id})" class="w-4 h-4 text-indigo-600 rounded border-slate-300 focus:ring-indigo-500 cursor-pointer">
            <div>
              <div class="flex items-center space-x-2">
                <span class="font-bold text-sm text-slate-800">${sec.curso ? sec.curso.nombre : 'Curso'}</span>
                <span class="px-2 py-0.5 rounded text-[10px] font-bold bg-indigo-100 text-indigo-700">${sec.codigoSeccion}</span>
                <span class="px-2 py-0.5 rounded text-[10px] font-semibold bg-slate-200 text-slate-600">Ciclo ${sec.curso ? sec.curso.ciclo : 1}</span>
              </div>
              <p class="text-xs text-slate-500 mt-0.5">
                <i class="fa-solid fa-user-tie text-[10px] mr-1"></i> ${sec.docente ? (sec.docente.nombres + ' ' + sec.docente.apellidos) : 'Por Asignar'}
                &bull; <i class="fa-solid fa-sun text-[10px] ml-1 mr-0.5"></i> ${sec.turno}
              </p>
            </div>
          </div>

          <div class="text-right">
            <p class="text-sm font-bold text-slate-800">S/ ${(sec.curso ? sec.curso.costo : 200).toFixed(2)}</p>
            <p class="text-xs font-semibold text-emerald-600">${vacantesLibres} vacantes libres</p>
            <p class="text-[11px] text-slate-400">${sec.curso ? sec.curso.creditos : 0} créditos</p>
          </div>
        `;
        contenedor.appendChild(item);
      });
    }
  } catch (err) {
    contenedor.innerHTML = '<p class="text-sm text-rose-500 text-center py-6">Error cargando secciones disponibles.</p>';
  }
}

function toggleSeleccionSeccion(seccionId) {
  const checkbox = document.getElementById(`sec-check-${seccionId}`);
  const sec = appState.seccionesDisponibles.find(x => x.id === seccionId);

  if (!sec) return;

  if (checkbox.checked) {
    // Validar si ya seleccionó otra sección del mismo curso
    for (let s of appState.seccionesSeleccionadas.values()) {
      if (s.curso && sec.curso && s.curso.id === sec.curso.id) {
        alert(`Ya tiene seleccionada la sección ${s.codigoSeccion} para el curso "${sec.curso.nombre}". Desmárquela primero si desea cambiar de sección.`);
        checkbox.checked = false;
        return;
      }
    }
    appState.seccionesSeleccionadas.set(seccionId, sec);
  } else {
    appState.seccionesSeleccionadas.delete(seccionId);
  }

  actualizarResumenMatricula();
}

function actualizarResumenMatricula() {
  const totalCursos = appState.seccionesSeleccionadas.size;
  let totalCreditos = 0;
  let costoTotal = 0;

  for (let s of appState.seccionesSeleccionadas.values()) {
    if (s.curso) {
      totalCreditos += s.curso.creditos || 0;
      costoTotal += s.curso.costo || 0;
    }
  }

  document.getElementById("resumen-total-cursos").textContent = totalCursos;
  document.getElementById("resumen-total-creditos").textContent = `${totalCreditos} cred.`;
  document.getElementById("resumen-costo-total").textContent = `S/ ${costoTotal.toFixed(2)}`;
}

async function confirmarMatricula() {
  const estudianteId = document.getElementById("select-estudiante").value;
  const periodoId = document.getElementById("select-periodo").value;
  const metodoPago = document.getElementById("select-metodo-pago").value;

  if (!estudianteId) {
    alert("Por favor seleccione un estudiante.");
    return;
  }
  if (!periodoId) {
    alert("Por favor seleccione un periodo académico.");
    return;
  }
  if (appState.seccionesSeleccionadas.size === 0) {
    alert("Por favor seleccione al menos una sección para matricular.");
    return;
  }

  const payload = {
    estudianteId: Number(estudianteId),
    periodoId: Number(periodoId),
    seccionIds: Array.from(appState.seccionesSeleccionadas.keys()),
    metodoPago: metodoPago
  };

  const btn = document.getElementById("btn-confirmar-matricula");
  btn.disabled = true;
  btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin mr-2"></i> Procesando...';

  try {
    const res = await fetch(`${API_BASE}/matriculas`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (res.ok && result.success) {
      mostrarToast(`¡Matrícula ${result.data.codigoMatricula} registrada con éxito!`, "success");
      // Limpiar y cambiar a tab de matriculas
      appState.seccionesSeleccionadas.clear();
      actualizarResumenMatricula();
      cargarSeccionesDisponibles();
      cargarDashboard();
      switchTab("matriculas");
    } else {
      mostrarToast(result.message || "Error al procesar la matrícula", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al procesar la matrícula", "error");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="fa-solid fa-check mr-2"></i> Confirmar y Matricular';
  }
}

// ==================== ESTUDIANTES (CRUD COMPLETO) ====================
async function cargarEstudiantes() {
  try {
    const res = await fetch(`${API_BASE}/estudiantes`);
    if (!res.ok) throw new Error("Error en solicitud");
    const { data } = await res.json();
    appState.estudiantes = data;
    renderEstudiantesTable(data);
  } catch (err) {
    mostrarToast("Error al cargar estudiantes", "error");
  }
}

function renderEstudiantesTable(lista) {
  const tbody = document.getElementById("estudiantes-tbody");
  tbody.innerHTML = "";

  if (lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="8" class="py-6 text-center text-slate-400">No se encontraron estudiantes</td></tr>`;
    return;
  }

  lista.forEach(e => {
    const tr = document.createElement("tr");
    tr.className = "hover:bg-slate-50 transition";
    tr.innerHTML = `
      <td class="py-3 px-4 font-bold text-indigo-700">${e.codigoEstudiante}</td>
      <td class="py-3 px-4 font-semibold text-slate-700">${e.dni}</td>
      <td class="py-3 px-4 font-medium text-slate-800">${e.nombres} ${e.apellidos}</td>
      <td class="py-3 px-4 text-slate-600">${e.carrera ? e.carrera.nombre : "--"}</td>
      <td class="py-3 px-4 text-slate-500 text-xs">${e.email}</td>
      <td class="py-3 px-4 text-slate-600">${e.telefono || "--"}</td>
      <td class="py-3 px-4">${renderBadgeEstado(e.estado)}</td>
      <td class="py-3 px-4 text-center space-x-1.5 whitespace-nowrap">
        <button onclick="editarEstudiante(${e.id})" title="Editar Estudiante" class="p-1.5 bg-amber-50 hover:bg-amber-100 text-amber-600 rounded-md transition">
          <i class="fa-solid fa-pen-to-square text-sm"></i>
        </button>
        <button onclick="eliminarEstudiante(${e.id}, '${e.nombres.replace(/'/g, "\\'")} ${e.apellidos.replace(/'/g, "\\'")}')" title="Eliminar Estudiante" class="p-1.5 bg-rose-50 hover:bg-rose-100 text-rose-600 rounded-md transition">
          <i class="fa-solid fa-trash text-sm"></i>
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function filtrarEstudiantes() {
  const q = document.getElementById("filtro-estudiantes").value.toLowerCase().trim();
  const res = appState.estudiantes.filter(e =>
    (e.dni && e.dni.includes(q)) ||
    (e.codigoEstudiante && e.codigoEstudiante.toLowerCase().includes(q)) ||
    (e.nombres && e.nombres.toLowerCase().includes(q)) ||
    (e.apellidos && e.apellidos.toLowerCase().includes(q))
  );
  renderEstudiantesTable(res);
}

async function cargarCarrerasSelect() {
  try {
    const res = await fetch(`${API_BASE}/carreras`);
    if (res.ok) {
      const { data } = await res.json();
      appState.carreras = data;
      const select = document.getElementById("nuevo-carrera-id");
      if (select) {
        select.innerHTML = '<option value="">Seleccione carrera...</option>';
        data.forEach(c => {
          const opt = document.createElement("option");
          opt.value = c.id;
          opt.textContent = `${c.nombre} (${c.facultad})`;
          select.appendChild(opt);
        });
      }
    }
  } catch (err) {
    console.error("Error cargando carreras:", err);
  }
}

function abrirModalNuevoEstudiante() {
  const form = document.getElementById("form-estudiante");
  if (form) form.reset();
  document.getElementById("estudiante-id").value = "";
  document.getElementById("modal-estudiante-titulo").textContent = "Registrar Nuevo Estudiante";
  document.getElementById("btn-estudiante-text").textContent = "Guardar Alumno";
  document.getElementById("nuevo-dni").disabled = false;
  document.getElementById("nuevo-estado-estudiante").value = "ACTIVO";
  document.getElementById("modal-nuevo-estudiante").classList.remove("hidden");
}

function cerrarModalNuevoEstudiante() {
  document.getElementById("modal-nuevo-estudiante").classList.add("hidden");
}

function editarEstudiante(id) {
  const e = appState.estudiantes.find(x => x.id === id);
  if (!e) return;

  document.getElementById("estudiante-id").value = e.id;
  document.getElementById("nuevo-dni").value = e.dni;
  document.getElementById("nuevo-codigo").value = e.codigoEstudiante || "";
  document.getElementById("nuevo-nombres").value = e.nombres;
  document.getElementById("nuevo-apellidos").value = e.apellidos;
  document.getElementById("nuevo-carrera-id").value = e.carrera ? e.carrera.id : "";
  document.getElementById("nuevo-estado-estudiante").value = e.estado || "ACTIVO";
  document.getElementById("nuevo-email").value = e.email;
  document.getElementById("nuevo-telefono").value = e.telefono || "";
  document.getElementById("nuevo-direccion").value = e.direccion || "";

  document.getElementById("modal-estudiante-titulo").textContent = "Editar Estudiante";
  document.getElementById("btn-estudiante-text").textContent = "Actualizar Alumno";
  document.getElementById("modal-nuevo-estudiante").classList.remove("hidden");
}

async function guardarEstudiante(event) {
  event.preventDefault();
  const id = document.getElementById("estudiante-id").value;
  const dni = document.getElementById("nuevo-dni").value.trim();
  const codigo = document.getElementById("nuevo-codigo").value.trim();
  const nombres = document.getElementById("nuevo-nombres").value.trim();
  const apellidos = document.getElementById("nuevo-apellidos").value.trim();
  const carreraId = document.getElementById("nuevo-carrera-id").value;
  const estado = document.getElementById("nuevo-estado-estudiante").value;
  const email = document.getElementById("nuevo-email").value.trim();
  const telefono = document.getElementById("nuevo-telefono").value.trim();
  const direccion = document.getElementById("nuevo-direccion").value.trim();

  const payload = {
    dni,
    codigoEstudiante: codigo || undefined,
    nombres,
    apellidos,
    carrera: { id: Number(carreraId) },
    email,
    telefono,
    direccion,
    estado
  };

  try {
    const url = id ? `${API_BASE}/estudiantes/${id}` : `${API_BASE}/estudiantes`;
    const method = id ? "PUT" : "POST";
    const res = await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (res.ok && result.success) {
      mostrarToast(id ? "Estudiante actualizado exitosamente" : "Estudiante registrado exitosamente", "success");
      cerrarModalNuevoEstudiante();
      document.getElementById("form-estudiante").reset();
      cargarEstudiantes();
      cargarEstudiantesSelect();
      cargarDashboard();
    } else {
      mostrarToast(result.message || "Error al procesar estudiante", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al guardar estudiante", "error");
  }
}

async function eliminarEstudiante(id, nombre) {
  if (!confirm(`¿Está seguro de que desea eliminar al estudiante "${nombre}"? Esta acción no se puede deshacer.`)) {
    return;
  }
  try {
    const res = await fetch(`${API_BASE}/estudiantes/${id}`, {
      method: "DELETE"
    });
    const result = await res.json();
    if (res.ok && result.success) {
      mostrarToast("Estudiante eliminado correctamente", "success");
      cargarEstudiantes();
      cargarEstudiantesSelect();
      cargarDashboard();
    } else {
      mostrarToast(result.message || "Error al eliminar estudiante", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al eliminar estudiante", "error");
  }
}

// ==================== CURSOS (CRUD COMPLETO) ====================
async function cargarPlanesSelect() {
  try {
    const res = await fetch(`${API_BASE}/planes-estudio`);
    if (res.ok) {
      const { data } = await res.json();
      appState.planes = data;
      const select = document.getElementById("curso-plan-id");
      if (select) {
        select.innerHTML = '<option value="">Seleccione plan de estudio...</option>';
        data.forEach(p => {
          const opt = document.createElement("option");
          opt.value = p.id;
          const carNom = p.carrera ? ` - ${p.carrera.nombre}` : "";
          opt.textContent = `${p.codigoPlan}${carNom}`;
          select.appendChild(opt);
        });
      }
    }
  } catch (err) {
    console.error("Error cargando planes de estudio:", err);
  }
}

async function cargarCursos() {
  try {
    const res = await fetch(`${API_BASE}/cursos`);
    if (!res.ok) throw new Error("Error en solicitud");
    const { data } = await res.json();
    appState.cursos = data;
    renderCursosTable(data);
  } catch (err) {
    mostrarToast("Error al cargar cursos", "error");
  }
}

function renderCursosTable(lista) {
  const tbody = document.getElementById("cursos-tbody");
  tbody.innerHTML = "";

  if (lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="9" class="py-6 text-center text-slate-400">No hay cursos disponibles</td></tr>`;
    return;
  }

  lista.forEach(c => {
    const tr = document.createElement("tr");
    tr.className = "hover:bg-slate-50 transition";
    tr.innerHTML = `
      <td class="py-3 px-4 font-bold text-indigo-700">${c.codigo}</td>
      <td class="py-3 px-4 font-semibold text-slate-800">${c.nombre}</td>
      <td class="py-3 px-4 text-slate-500 text-xs">${c.planEstudio ? c.planEstudio.codigoPlan : "--"}</td>
      <td class="py-3 px-4 text-center font-bold text-slate-700">Ciclo ${c.ciclo}</td>
      <td class="py-3 px-4 text-center font-bold text-indigo-600">${c.creditos}</td>
      <td class="py-3 px-4 text-center text-xs text-slate-500">${c.horasTeoria}T / ${c.horasPractica}P</td>
      <td class="py-3 px-4 font-bold text-slate-800">S/ ${(c.costo || 0).toFixed(2)}</td>
      <td class="py-3 px-4">${renderBadgeEstado(c.estado)}</td>
      <td class="py-3 px-4 text-center space-x-1.5 whitespace-nowrap">
        <button onclick="editarCurso(${c.id})" title="Editar Curso" class="p-1.5 bg-amber-50 hover:bg-amber-100 text-amber-600 rounded-md transition">
          <i class="fa-solid fa-pen-to-square text-sm"></i>
        </button>
        <button onclick="eliminarCurso(${c.id}, '${c.nombre.replace(/'/g, "\\'")}')" title="Eliminar Curso" class="p-1.5 bg-rose-50 hover:bg-rose-100 text-rose-600 rounded-md transition">
          <i class="fa-solid fa-trash text-sm"></i>
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function filtrarCursos() {
  const q = document.getElementById("filtro-cursos").value.toLowerCase().trim();
  const res = appState.cursos.filter(c =>
    (c.codigo && c.codigo.toLowerCase().includes(q)) ||
    (c.nombre && c.nombre.toLowerCase().includes(q)) ||
    (c.planEstudio && c.planEstudio.codigoPlan && c.planEstudio.codigoPlan.toLowerCase().includes(q))
  );
  renderCursosTable(res);
}

function abrirModalNuevoCurso() {
  const form = document.getElementById("form-curso");
  if (form) form.reset();
  document.getElementById("curso-id").value = "";
  document.getElementById("modal-curso-titulo").textContent = "Registrar Nuevo Curso";
  document.getElementById("btn-curso-text").textContent = "Guardar Asignatura";
  document.getElementById("curso-estado").value = "ACTIVO";
  document.getElementById("modal-curso").classList.remove("hidden");
}

function cerrarModalCurso() {
  document.getElementById("modal-curso").classList.add("hidden");
}

function editarCurso(id) {
  const c = appState.cursos.find(x => x.id === id);
  if (!c) return;

  document.getElementById("curso-id").value = c.id;
  document.getElementById("curso-codigo").value = c.codigo;
  document.getElementById("curso-plan-id").value = c.planEstudio ? c.planEstudio.id : "";
  document.getElementById("curso-nombre").value = c.nombre;
  document.getElementById("curso-ciclo").value = c.ciclo;
  document.getElementById("curso-creditos").value = c.creditos;
  document.getElementById("curso-costo").value = c.costo;
  document.getElementById("curso-horas-teoria").value = c.horasTeoria;
  document.getElementById("curso-horas-practica").value = c.horasPractica;
  document.getElementById("curso-estado").value = c.estado || "ACTIVO";

  document.getElementById("modal-curso-titulo").textContent = "Editar Asignatura";
  document.getElementById("btn-curso-text").textContent = "Actualizar Asignatura";
  document.getElementById("modal-curso").classList.remove("hidden");
}

async function guardarCurso(event) {
  event.preventDefault();
  const id = document.getElementById("curso-id").value;
  const codigo = document.getElementById("curso-codigo").value.trim();
  const planId = document.getElementById("curso-plan-id").value;
  const nombre = document.getElementById("curso-nombre").value.trim();
  const ciclo = Number(document.getElementById("curso-ciclo").value);
  const creditos = Number(document.getElementById("curso-creditos").value);
  const costo = parseFloat(document.getElementById("curso-costo").value);
  const horasTeoria = Number(document.getElementById("curso-horas-teoria").value);
  const horasPractica = Number(document.getElementById("curso-horas-practica").value);
  const estado = document.getElementById("curso-estado").value;

  const payload = {
    codigo,
    planEstudio: { id: Number(planId) },
    nombre,
    ciclo,
    creditos,
    costo,
    horasTeoria,
    horasPractica,
    estado
  };

  try {
    const url = id ? `${API_BASE}/cursos/${id}` : `${API_BASE}/cursos`;
    const method = id ? "PUT" : "POST";
    const res = await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (res.ok && result.success) {
      mostrarToast(id ? "Asignatura actualizada exitosamente" : "Asignatura registrada exitosamente", "success");
      cerrarModalCurso();
      document.getElementById("form-curso").reset();
      cargarCursos();
      cargarDashboard();
    } else {
      mostrarToast(result.message || "Error al procesar asignatura", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al guardar asignatura", "error");
  }
}

async function eliminarCurso(id, nombre) {
  if (!confirm(`¿Está seguro de que desea eliminar la asignatura "${nombre}"? Esta acción no se puede deshacer.`)) {
    return;
  }
  try {
    const res = await fetch(`${API_BASE}/cursos/${id}`, {
      method: "DELETE"
    });
    const result = await res.json();
    if (res.ok && result.success) {
      mostrarToast("Asignatura eliminada correctamente", "success");
      cargarCursos();
      cargarDashboard();
    } else {
      mostrarToast(result.message || "Error al eliminar asignatura", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al eliminar asignatura", "error");
  }
}

// ==================== DOCENTES (CRUD COMPLETO) ====================
async function cargarDocentes() {
  try {
    const res = await fetch(`${API_BASE}/docentes`);
    if (!res.ok) throw new Error("Error en solicitud");
    const { data } = await res.json();
    appState.docentes = data;
    renderDocentesCards(data);
  } catch (err) {
    mostrarToast("Error al cargar docentes", "error");
  }
}

function renderDocentesCards(lista) {
  const container = document.getElementById("docentes-cards-container");
  container.innerHTML = "";

  if (lista.length === 0) {
    container.innerHTML = `<div class="col-span-3 py-6 text-center text-slate-400">No se encontraron docentes registrados</div>`;
    return;
  }

  lista.forEach(d => {
    const card = document.createElement("div");
    card.className = "bg-white p-5 rounded-xl shadow-sm border border-slate-200 flex flex-col justify-between";
    card.innerHTML = `
      <div>
        <div class="flex items-start justify-between">
          <div>
            <span class="inline-block px-2.5 py-0.5 rounded-full text-[11px] font-semibold bg-indigo-50 text-indigo-700 mb-2">
              ${d.gradoAcademico}
            </span>
            <h4 class="font-bold text-base text-slate-800">${d.nombres} ${d.apellidos}</h4>
            <p class="text-xs text-indigo-600 font-medium">${d.especialidad}</p>
          </div>
          <div class="w-10 h-10 rounded-full bg-slate-100 flex items-center justify-center text-slate-600 text-lg">
            <i class="fa-solid fa-user-tie"></i>
          </div>
        </div>
        <div class="mt-4 pt-3 border-t border-slate-100 space-y-1 text-xs text-slate-500">
          <p><i class="fa-solid fa-id-card w-4 text-slate-400"></i> DNI: <span class="font-semibold text-slate-700">${d.dni}</span></p>
          <p><i class="fa-solid fa-envelope w-4 text-slate-400"></i> ${d.email}</p>
          <p><i class="fa-solid fa-phone w-4 text-slate-400"></i> ${d.telefono || "--"}</p>
        </div>
      </div>
      <div class="mt-4 pt-3 border-t border-slate-100 flex justify-between items-center text-xs">
        <div class="flex items-center space-x-1.5">
          <span class="text-slate-400">Estado:</span>
          ${renderBadgeEstado(d.estado)}
        </div>
        <div class="flex items-center space-x-1">
          <button onclick="editarDocente(${d.id})" title="Editar Docente" class="p-1.5 bg-amber-50 hover:bg-amber-100 text-amber-600 rounded-md transition text-xs flex items-center font-medium">
            <i class="fa-solid fa-pen-to-square mr-1"></i> Editar
          </button>
          <button onclick="eliminarDocente(${d.id}, '${d.nombres.replace(/'/g, "\\'")} ${d.apellidos.replace(/'/g, "\\'")}')" title="Eliminar Docente" class="p-1.5 bg-rose-50 hover:bg-rose-100 text-rose-600 rounded-md transition text-xs flex items-center font-medium">
            <i class="fa-solid fa-trash mr-1"></i> Borrar
          </button>
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

function filtrarDocentes() {
  const q = document.getElementById("filtro-docentes").value.toLowerCase().trim();
  const res = appState.docentes.filter(d =>
    (d.nombres && d.nombres.toLowerCase().includes(q)) ||
    (d.apellidos && d.apellidos.toLowerCase().includes(q)) ||
    (d.dni && d.dni.includes(q)) ||
    (d.especialidad && d.especialidad.toLowerCase().includes(q))
  );
  renderDocentesCards(res);
}

function abrirModalNuevoDocente() {
  const form = document.getElementById("form-docente");
  if (form) form.reset();
  document.getElementById("docente-id").value = "";
  document.getElementById("modal-docente-titulo").textContent = "Registrar Nuevo Docente";
  document.getElementById("btn-docente-text").textContent = "Guardar Docente";
  document.getElementById("docente-grado").value = "Magíster";
  document.getElementById("docente-estado").value = "ACTIVO";
  document.getElementById("modal-docente").classList.remove("hidden");
}

function cerrarModalDocente() {
  document.getElementById("modal-docente").classList.add("hidden");
}

function editarDocente(id) {
  const d = appState.docentes.find(x => x.id === id);
  if (!d) return;

  document.getElementById("docente-id").value = d.id;
  document.getElementById("docente-dni").value = d.dni;
  document.getElementById("docente-grado").value = d.gradoAcademico || "Magíster";
  document.getElementById("docente-nombres").value = d.nombres;
  document.getElementById("docente-apellidos").value = d.apellidos;
  document.getElementById("docente-especialidad").value = d.especialidad;
  document.getElementById("docente-estado").value = d.estado || "ACTIVO";
  document.getElementById("docente-email").value = d.email;
  document.getElementById("docente-telefono").value = d.telefono || "";

  document.getElementById("modal-docente-titulo").textContent = "Editar Docente";
  document.getElementById("btn-docente-text").textContent = "Actualizar Docente";
  document.getElementById("modal-docente").classList.remove("hidden");
}

async function guardarDocente(event) {
  event.preventDefault();
  const id = document.getElementById("docente-id").value;
  const dni = document.getElementById("docente-dni").value.trim();
  const gradoAcademico = document.getElementById("docente-grado").value;
  const nombres = document.getElementById("docente-nombres").value.trim();
  const apellidos = document.getElementById("docente-apellidos").value.trim();
  const especialidad = document.getElementById("docente-especialidad").value.trim();
  const estado = document.getElementById("docente-estado").value;
  const email = document.getElementById("docente-email").value.trim();
  const telefono = document.getElementById("docente-telefono").value.trim();

  const payload = {
    dni,
    gradoAcademico,
    nombres,
    apellidos,
    especialidad,
    estado,
    email,
    telefono
  };

  try {
    const url = id ? `${API_BASE}/docentes/${id}` : `${API_BASE}/docentes`;
    const method = id ? "PUT" : "POST";
    const res = await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    const result = await res.json();

    if (res.ok && result.success) {
      mostrarToast(id ? "Docente actualizado exitosamente" : "Docente registrado exitosamente", "success");
      cerrarModalDocente();
      document.getElementById("form-docente").reset();
      cargarDocentes();
    } else {
      mostrarToast(result.message || "Error al procesar docente", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al guardar docente", "error");
  }
}

async function eliminarDocente(id, nombre) {
  if (!confirm(`¿Está seguro de que desea eliminar al docente "${nombre}"? Esta acción no se puede deshacer.`)) {
    return;
  }
  try {
    const res = await fetch(`${API_BASE}/docentes/${id}`, {
      method: "DELETE"
    });
    const result = await res.json();
    if (res.ok && result.success) {
      mostrarToast("Docente eliminado correctamente", "success");
      cargarDocentes();
    } else {
      mostrarToast(result.message || "Error al eliminar docente", "error");
    }
  } catch (err) {
    mostrarToast("Error de conexión al eliminar docente", "error");
  }
}

// ==================== UTILITARIOS ====================
function renderBadgeEstado(estado) {
  const est = (estado || "").toUpperCase();
  if (est === "ACTIVO" || est === "PAGADA" || est === "PAGADO") {
    return `<span class="px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-100 text-emerald-800">${est}</span>`;
  }
  if (est === "CONFIRMADA" || est === "REGISTRADA") {
    return `<span class="px-2.5 py-1 rounded-full text-xs font-semibold bg-indigo-100 text-indigo-800">${est}</span>`;
  }
  if (est === "ANULADA" || est === "INACTIVO") {
    return `<span class="px-2.5 py-1 rounded-full text-xs font-semibold bg-rose-100 text-rose-800">${est}</span>`;
  }
  return `<span class="px-2.5 py-1 rounded-full text-xs font-semibold bg-slate-100 text-slate-800">${est}</span>`;
}

function mostrarToast(mensaje, tipo = "info") {
  const toast = document.getElementById("toast");
  const msgEl = document.getElementById("toast-msg");
  const iconEl = document.getElementById("toast-icon");

  msgEl.textContent = mensaje;
  if (tipo === "success") {
    iconEl.className = "fa-solid fa-check-circle text-emerald-400 text-xl";
  } else if (tipo === "error") {
    iconEl.className = "fa-solid fa-triangle-exclamation text-rose-400 text-xl";
  } else {
    iconEl.className = "fa-solid fa-info-circle text-sky-400 text-xl";
  }

  toast.classList.remove("hidden");
  toast.classList.remove("opacity-0");
  setTimeout(() => {
    toast.classList.add("opacity-0");
    setTimeout(() => toast.classList.add("hidden"), 300);
  }, 3500);
}
