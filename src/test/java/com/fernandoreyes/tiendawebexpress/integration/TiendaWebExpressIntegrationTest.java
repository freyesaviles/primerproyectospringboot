package com.fernandoreyes.tiendawebexpress.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fernandoreyes.tiendawebexpress.entity.Categoria;
import com.fernandoreyes.tiendawebexpress.entity.Pedido;
import com.fernandoreyes.tiendawebexpress.entity.Producto;
import com.fernandoreyes.tiendawebexpress.repository.CategoriaRepository;
import com.fernandoreyes.tiendawebexpress.repository.PedidoRepository;
import com.fernandoreyes.tiendawebexpress.repository.ProductoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TiendaWebExpressIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private Categoria tecnologia;
    private Categoria ropa;
    private Producto mouse;
    private Producto teclado;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();

        tecnologia = categoriaRepository.save(categoria("Tecnología"));
        ropa = categoriaRepository.save(categoria("Ropa"));
        Categoria hogar = categoriaRepository.save(categoria("Hogar"));
        Categoria accesorios = categoriaRepository.save(categoria("Accesorios"));

        mouse = productoRepository.save(producto("Mouse inalámbrico", "Mouse ergonómico con conexión USB.", "mouse.jpg", new BigDecimal("12.99"), 25, tecnologia));
        teclado = productoRepository.save(producto("Teclado mecánico", "Teclado mecánico con iluminación LED.", "teclado.jpg", new BigDecimal("39.99"), 15, tecnologia));
        productoRepository.save(producto("Camiseta básica", "Camiseta de algodón disponible en varias tallas.", "camiseta.jpg", new BigDecimal("9.50"), 40, ropa));
        productoRepository.save(producto("Taza personalizada", "Taza de cerámica ideal para regalos.", "taza.jpg", new BigDecimal("6.75"), 30, hogar));
        productoRepository.save(producto("Mochila urbana", "Mochila resistente para uso diario.", "mochila.jpg", new BigDecimal("24.99"), 20, accesorios));
    }

    @Test
    void inicioMuestraNombreDeTiendaYDestacados() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("categorias"))
            .andExpect(model().attributeExists("destacados"));
    }

    @Test
    void catalogoMuestraListaYFiltros() throws Exception {
        mockMvc.perform(get("/catalogo"))
            .andExpect(status().isOk())
            .andExpect(view().name("catalogo"))
            .andExpect(model().attributeExists("productos"))
            .andExpect(model().attributeExists("categorias", "searchTerm"))
            .andExpect(model().attribute("searchTerm", ""))
            .andExpect(model().attribute("selectedCategoryId", nullValue()));
    }

    @Test
    void catalogoBuscaPorNombreYMantieneFiltro() throws Exception {
        mockMvc.perform(get("/catalogo").param("nombre", " mouse "))
            .andExpect(status().isOk())
            .andExpect(view().name("catalogo"))
            .andExpect(model().attributeExists("productos", "categorias", "searchTerm"))
            .andExpect(model().attribute("searchTerm", "mouse"))
            .andExpect(model().attribute("selectedCategoryId", nullValue()))
            .andExpect(model().attribute("productos", hasSize(1)))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Mouse inalámbrico")))
            .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Teclado mecánico"))));
    }

    @Test
    void catalogoFiltraPorCategoriaYMantieneSeleccion() throws Exception {
        mockMvc.perform(get("/catalogo").param("categoria", tecnologia.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("catalogo"))
            .andExpect(model().attributeExists("productos", "categorias", "searchTerm", "selectedCategoryId"))
            .andExpect(model().attribute("searchTerm", ""))
            .andExpect(model().attribute("selectedCategoryId", tecnologia.getId()))
            .andExpect(model().attribute("productos", hasSize(2)))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Mouse inalámbrico")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Teclado mecánico")))
            .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Camiseta básica"))));
    }

    @Test
    void catalogoCombinaNombreYCategoriaConCoincidencias() throws Exception {
        mockMvc.perform(get("/catalogo")
                .param("nombre", "mouse")
                .param("categoria", tecnologia.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("catalogo"))
            .andExpect(model().attribute("searchTerm", "mouse"))
            .andExpect(model().attribute("selectedCategoryId", tecnologia.getId()))
            .andExpect(model().attribute("productos", hasSize(1)))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Mouse inalámbrico")))
            .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Teclado mecánico"))));
    }

    @Test
    void catalogoCombinaNombreYCategoriaSinCoincidencias() throws Exception {
        mockMvc.perform(get("/catalogo")
                .param("nombre", "mouse")
                .param("categoria", ropa.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("catalogo"))
            .andExpect(model().attribute("searchTerm", "mouse"))
            .andExpect(model().attribute("selectedCategoryId", ropa.getId()))
            .andExpect(model().attribute("productos", hasSize(0)))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("No se encontraron productos.")));
    }

    @Test
    void detalleInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/productos/999"))
            .andExpect(status().isNotFound())
            .andExpect(view().name("error/404"));
    }

    @Test
    void crearPedidoPersisteDatosYDescuentaStock() throws Exception {
        mockMvc.perform(post("/pedido")
                .param("productoId", mouse.getId().toString())
                .param("cantidad", "2")
                .param("nombreCliente", "Ana Pérez")
                .param("correo", "ana@correo.com")
                .param("comentario", "Entregar por la tarde"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/pedido/confirmacion/*"));

        assertThat(pedidoRepository.findAll()).hasSize(1);
        Pedido pedido = pedidoRepository.findById(pedidoRepository.findAll().getFirst().getId()).orElseThrow();
        assertThat(pedido.getTotal()).isEqualByComparingTo("25.98");
        assertThat(pedido.getDetalles()).hasSize(1);
        assertThat(productoRepository.findById(mouse.getId()).orElseThrow().getStock()).isEqualTo(23);
    }

    @Test
    void crearPedidoConDatosInvalidosMuestraErrores() throws Exception {
        mockMvc.perform(post("/pedido")
                .param("productoId", "")
                .param("cantidad", "0")
                .param("nombreCliente", "")
                .param("correo", "correo-invalido")
                .param("comentario", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("pedido"))
            .andExpect(model().attributeHasFieldErrors("pedidoForm", "productoId", "cantidad", "nombreCliente", "correo"));
    }

    @Test
    void crearPedidoSinStockSuficienteConservaFormulario() throws Exception {
        mockMvc.perform(post("/pedido")
                .param("productoId", teclado.getId().toString())
                .param("cantidad", "99")
                .param("nombreCliente", "Luis")
                .param("correo", "luis@correo.com")
                .param("comentario", "Prueba"))
            .andExpect(status().isOk())
            .andExpect(view().name("pedido"))
            .andExpect(model().attributeHasFieldErrors("pedidoForm", "cantidad"));

        assertThat(pedidoRepository.findAll()).isEmpty();
    }

    private Categoria categoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        return categoria;
    }

    private Producto producto(String nombre, String descripcion, String imagen, BigDecimal precio, int stock, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setImagen(imagen);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoria);
        return producto;
    }
}
