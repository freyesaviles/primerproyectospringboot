const STORAGE_KEY = "tienda-web-express:cart-items";

const menuToggle = document.querySelector(".menu-toggle");
const siteNav = document.querySelector(".site-nav");
const toast = document.querySelector("#toast");
const countNode = document.querySelector("#selected-count");
const cartToggle = document.querySelector("#cart-toggle");
const cartPreview = document.querySelector("#cart-preview");
const cartItemsNode = document.querySelector("#cart-items");
const cartEmptyNode = document.querySelector("#cart-empty");
const orderForm = document.querySelector("#order-form");
const addToCartButton = document.querySelector(".add-to-cart");

let toastTimer;

function getCartItems() {
    try {
        return JSON.parse(sessionStorage.getItem(STORAGE_KEY) || "[]");
    } catch {
        return [];
    }
}

function saveCartItems(items) {
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify(items));
}

function renderCart() {
    const items = getCartItems();

    if (countNode) {
        countNode.textContent = String(items.length);
    }

    if (!cartItemsNode || !cartEmptyNode) {
        return;
    }

    cartItemsNode.innerHTML = items
        .map((item) => `<li>${item}</li>`)
        .join("");

    cartEmptyNode.hidden = items.length > 0;
    cartItemsNode.hidden = items.length === 0;
}

function addProductToCart(productName) {
    const items = getCartItems();
    items.push(productName);
    saveCartItems(items);
    renderCart();
}

function initCartPreview() {
    renderCart();

    if (!cartToggle || !cartPreview) {
        return;
    }

    cartToggle.addEventListener("click", () => {
        const expanded = cartToggle.getAttribute("aria-expanded") === "true";
        cartToggle.setAttribute("aria-expanded", String(!expanded));
        cartPreview.hidden = expanded;
    });

    document.addEventListener("click", (event) => {
        if (!cartPreview || !cartToggle) {
            return;
        }

        const target = event.target;
        if (!(target instanceof Node)) {
            return;
        }

        if (!cartPreview.contains(target) && !cartToggle.contains(target)) {
            cartPreview.hidden = true;
            cartToggle.setAttribute("aria-expanded", "false");
        }
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            cartPreview.hidden = true;
            cartToggle.setAttribute("aria-expanded", "false");
        }
    });
}

function showToast(message) {
    if (!toast) {
        return;
    }

    toast.textContent = message;
    toast.classList.add("is-visible");
    toast.setAttribute("aria-hidden", "false");

    window.clearTimeout(toastTimer);
    toastTimer = window.setTimeout(() => {
        toast.classList.remove("is-visible");
        toast.setAttribute("aria-hidden", "true");
    }, 2600);
}

function initMenu() {
    if (!menuToggle || !siteNav) {
        return;
    }

    menuToggle.addEventListener("click", () => {
        const expanded = menuToggle.getAttribute("aria-expanded") === "true";
        menuToggle.setAttribute("aria-expanded", String(!expanded));
        siteNav.classList.toggle("is-open");
    });

    siteNav.addEventListener("click", (event) => {
        if (event.target instanceof HTMLAnchorElement) {
            siteNav.classList.remove("is-open");
            menuToggle.setAttribute("aria-expanded", "false");
        }
    });
}

function initCounter() {
    if (!addToCartButton) {
        return;
    }

    addToCartButton.addEventListener("click", () => {
        const productName = addToCartButton.dataset.productName || "Producto";
        addProductToCart(productName);
        showToast(`${productName} fue agregado al carrito.`);
    });
}

function initCategoryFilter() {
    const filterContainer = document.querySelector("#category-filters");
    const cards = document.querySelectorAll(".catalog-card");

    if (!filterContainer || cards.length === 0) {
        return;
    }

    const applyFilter = (filterValue) => {
        cards.forEach((card) => {
            const categoryId = card.dataset.categoriaId;
            const shouldShow = filterValue === "all" || categoryId === filterValue;
            card.hidden = !shouldShow;
        });

        filterContainer.querySelectorAll(".filter-button").forEach((button) => {
            button.classList.toggle("is-active", button.dataset.filter === filterValue);
        });
    };

    filterContainer.addEventListener("click", (event) => {
        const button = event.target.closest(".filter-button");
        if (!button) {
            return;
        }
        applyFilter(button.dataset.filter || "all");
    });

    const preselected = filterContainer.querySelector(".filter-button.is-active");
    applyFilter(preselected?.dataset.filter || "all");
}

function initToggleDetails() {
    document.querySelectorAll("[data-toggle-details]").forEach((button) => {
        button.addEventListener("click", () => {
            const targetId = button.getAttribute("data-target");
            if (!targetId) {
                return;
            }

            const panel = document.getElementById(targetId);
            if (!panel) {
                return;
            }

            const isHidden = panel.hasAttribute("hidden");
            panel.toggleAttribute("hidden");
            button.textContent = isHidden ? "Ocultar detalle rápido" : "Mostrar detalle rápido";
        });
    });
}

function initOrderValidation() {
    if (!orderForm) {
        return;
    }

    orderForm.addEventListener("submit", (event) => {
        const product = document.querySelector("#productoId");
        const quantity = document.querySelector("#cantidad");
        const customerName = document.querySelector("#nombreCliente");
        const email = document.querySelector("#correo");

        const errors = [];

        if (!product?.value) {
            errors.push("Selecciona un producto.");
        }

        if (!quantity?.value || Number(quantity.value) < 1) {
            errors.push("La cantidad debe ser al menos 1.");
        }

        if (!customerName?.value.trim()) {
            errors.push("Ingresa tu nombre.");
        }

        const emailValue = email?.value.trim() || "";
        const validEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailValue);
        if (!validEmail) {
            errors.push("Ingresa un correo valido.");
        }

        if (errors.length > 0) {
            event.preventDefault();
            showToast(errors[0]);
        }
    });
}

initMenu();
initCartPreview();
initCounter();
initCategoryFilter();
initToggleDetails();
initOrderValidation();
