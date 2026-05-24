const menuToggle = document.querySelector(".menu-toggle");
const siteNav = document.querySelector(".site-nav");
const toast = document.querySelector("#toast");
const orderForm = document.querySelector("#order-form");

let toastTimer;

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

        if (product && !product.value) {
            errors.push("Selecciona un producto.");
        }

        if (quantity && (!quantity.value || Number(quantity.value) < 1)) {
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
initToggleDetails();
initOrderValidation();
