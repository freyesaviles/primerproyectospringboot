const productos = [
  {
    id: 1,
    nombre: "Arroz Selecto",
    categoria: "Granos",
    precio: "$2.50",
    imagen: "assets/images/arroz.svg",
    descripcion: "Arroz de grano largo, ideal para el consumo diario del hogar."
  },
  {
    id: 2,
    nombre: "Frijoles Rojos",
    categoria: "Granos",
    precio: "$1.95",
    imagen: "assets/images/frijoles.svg",
    descripcion: "Frijoles suaves y rendidores para acompanamientos y platos tradicionales."
  },
  {
    id: 3,
    nombre: "Jugo de Mango",
    categoria: "Bebidas",
    precio: "$1.40",
    imagen: "assets/images/jugo.svg",
    descripcion: "Bebida refrescante con sabor frutal, pensada para acompanar cualquier comida."
  },
  {
    id: 4,
    nombre: "Cafe Molido",
    categoria: "Bebidas",
    precio: "$3.25",
    imagen: "assets/images/cafe.svg",
    descripcion: "Cafe aromatico de tueste medio para las mananas y tardes de la casa."
  },
  {
    id: 5,
    nombre: "Jabon Multiuso",
    categoria: "Limpieza",
    precio: "$2.10",
    imagen: "assets/images/jabon.svg",
    descripcion: "Producto basico para el aseo del hogar, practico y facil de usar."
  },
  {
    id: 6,
    nombre: "Galletas de Avena",
    categoria: "Snacks",
    precio: "$1.75",
    imagen: "assets/images/galletas.svg",
    descripcion: "Snack ligero y familiar, ideal para meriendas o acompanar bebidas calientes."
  }
];

const productGrid = document.querySelector("#product-grid");
const productSelect = document.querySelector("#producto");
const orderForm = document.querySelector("#order-form");
const orderMessage = document.querySelector("#order-message");
const modal = document.querySelector("#product-modal");
const modalClose = document.querySelector("#modal-close");
const modalImage = document.querySelector("#modal-image");
const modalCategory = document.querySelector("#modal-category");
const modalName = document.querySelector("#modal-name");
const modalPrice = document.querySelector("#modal-price");
const modalDescription = document.querySelector("#modal-description");
const modalOrderButton = document.querySelector("#modal-order-button");
const menuToggle = document.querySelector(".menu-toggle");
const siteNav = document.querySelector(".site-nav");

let productoSeleccionado = productos[0];
let pedidoActual = {
  producto: productos[0].nombre,
  cantidad: 1,
  nombre: "",
  telefono: ""
};

function renderProductos() {
  productGrid.innerHTML = productos
    .map(
      (producto) => `
        <article class="product-card">
          <img src="${producto.imagen}" alt="${producto.nombre}" />
          <div class="product-body">
            <div class="product-topline">
              <span class="product-category">${producto.categoria}</span>
              <span class="product-price">${producto.precio}</span>
            </div>
            <h3>${producto.nombre}</h3>
            <p>${producto.descripcion}</p>
            <button class="button button-secondary" type="button" data-product-id="${producto.id}">
              Ver detalle
            </button>
          </div>
        </article>
      `
    )
    .join("");
}

function renderSelectProductos() {
  productSelect.innerHTML = productos
    .map(
      (producto) => `
        <option value="${producto.nombre}">${producto.nombre} - ${producto.precio}</option>
      `
    )
    .join("");

  productSelect.value = pedidoActual.producto;
}

function abrirModal(producto) {
  productoSeleccionado = producto;
  modalImage.src = producto.imagen;
  modalImage.alt = producto.nombre;
  modalCategory.textContent = producto.categoria;
  modalName.textContent = producto.nombre;
  modalPrice.textContent = producto.precio;
  modalDescription.textContent = producto.descripcion;
  modal.classList.add("is-open");
  modal.setAttribute("aria-hidden", "false");
  document.body.classList.add("modal-open");
}

function cerrarModal() {
  modal.classList.remove("is-open");
  modal.setAttribute("aria-hidden", "true");
  document.body.classList.remove("modal-open");
}

function cargarProductoEnPedido(producto) {
  pedidoActual.producto = producto.nombre;
  productSelect.value = producto.nombre;
  document.querySelector("#cantidad").value = 1;
  pedidoActual.cantidad = 1;
  orderMessage.textContent = `${producto.nombre} fue agregado al formulario de pedido.`;
  cerrarModal();
  document.querySelector("#pedido").scrollIntoView({ behavior: "smooth" });
}

function inicializarEventos() {
  productGrid.addEventListener("click", (event) => {
    const button = event.target.closest("[data-product-id]");

    if (!button) {
      return;
    }

    const producto = productos.find((item) => item.id === Number(button.dataset.productId));

    if (producto) {
      abrirModal(producto);
    }
  });

  modalClose.addEventListener("click", cerrarModal);
  modal.addEventListener("click", (event) => {
    if (event.target.hasAttribute("data-close-modal")) {
      cerrarModal();
    }
  });

  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && modal.classList.contains("is-open")) {
      cerrarModal();
    }
  });

  modalOrderButton.addEventListener("click", () => {
    cargarProductoEnPedido(productoSeleccionado);
  });

  productSelect.addEventListener("change", (event) => {
    pedidoActual.producto = event.target.value;
  });

  orderForm.addEventListener("submit", (event) => {
    event.preventDefault();

    const formData = new FormData(orderForm);
    pedidoActual = {
      producto: formData.get("producto"),
      cantidad: formData.get("cantidad"),
      nombre: formData.get("nombre"),
      telefono: formData.get("telefono")
    };

    orderMessage.textContent =
      `Pedido simulado: ${pedidoActual.nombre} solicito ${pedidoActual.cantidad} unidad(es) de ${pedidoActual.producto}.`;

    orderForm.reset();
    productSelect.value = productos[0].nombre;
    document.querySelector("#cantidad").value = 1;
    pedidoActual.producto = productos[0].nombre;
  });

  menuToggle.addEventListener("click", () => {
    const expanded = menuToggle.getAttribute("aria-expanded") === "true";

    menuToggle.setAttribute("aria-expanded", String(!expanded));
    siteNav.classList.toggle("is-open");
  });

  siteNav.addEventListener("click", (event) => {
    if (event.target.tagName === "A") {
      siteNav.classList.remove("is-open");
      menuToggle.setAttribute("aria-expanded", "false");
    }
  });
}

renderProductos();
renderSelectProductos();
inicializarEventos();
