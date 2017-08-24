package pe.com.hatcc.ms.posts.web.rest;

import pe.com.hatcc.ms.posts.MsPostsHatccApp;

import pe.com.hatcc.ms.posts.domain.Contexto;
import pe.com.hatcc.ms.posts.repository.ContextoRepository;
import pe.com.hatcc.ms.posts.service.ContextoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContextoResource REST controller.
 *
 * @see ContextoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsPostsHatccApp.class)
public class ContextoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private ContextoRepository contextoRepository;

    @Inject
    private ContextoService contextoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContextoMockMvc;

    private Contexto contexto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContextoResource contextoResource = new ContextoResource();
        ReflectionTestUtils.setField(contextoResource, "contextoService", contextoService);
        this.restContextoMockMvc = MockMvcBuilders.standaloneSetup(contextoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contexto createEntity() {
        Contexto contexto = new Contexto()
                .nombre(DEFAULT_NOMBRE)
                .descripcion(DEFAULT_DESCRIPCION);
        return contexto;
    }

    @Before
    public void initTest() {
        contextoRepository.deleteAll();
        contexto = createEntity();
    }

    @Test
    public void createContexto() throws Exception {
        int databaseSizeBeforeCreate = contextoRepository.findAll().size();

        // Create the Contexto

        restContextoMockMvc.perform(post("/api/contextos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contexto)))
                .andExpect(status().isCreated());

        // Validate the Contexto in the database
        List<Contexto> contextos = contextoRepository.findAll();
        assertThat(contextos).hasSize(databaseSizeBeforeCreate + 1);
        Contexto testContexto = contextos.get(contextos.size() - 1);
        assertThat(testContexto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testContexto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    public void getAllContextos() throws Exception {
        // Initialize the database
        contextoRepository.save(contexto);

        // Get all the contextos
        restContextoMockMvc.perform(get("/api/contextos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contexto.getId())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    public void getContexto() throws Exception {
        // Initialize the database
        contextoRepository.save(contexto);

        // Get the contexto
        restContextoMockMvc.perform(get("/api/contextos/{id}", contexto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contexto.getId()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    public void getNonExistingContexto() throws Exception {
        // Get the contexto
        restContextoMockMvc.perform(get("/api/contextos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateContexto() throws Exception {
        // Initialize the database
        contextoService.save(contexto);

        int databaseSizeBeforeUpdate = contextoRepository.findAll().size();

        // Update the contexto
        Contexto updatedContexto = contextoRepository.findOne(contexto.getId());
        updatedContexto
                .nombre(UPDATED_NOMBRE)
                .descripcion(UPDATED_DESCRIPCION);

        restContextoMockMvc.perform(put("/api/contextos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContexto)))
                .andExpect(status().isOk());

        // Validate the Contexto in the database
        List<Contexto> contextos = contextoRepository.findAll();
        assertThat(contextos).hasSize(databaseSizeBeforeUpdate);
        Contexto testContexto = contextos.get(contextos.size() - 1);
        assertThat(testContexto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testContexto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    public void deleteContexto() throws Exception {
        // Initialize the database
        contextoService.save(contexto);

        int databaseSizeBeforeDelete = contextoRepository.findAll().size();

        // Get the contexto
        restContextoMockMvc.perform(delete("/api/contextos/{id}", contexto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Contexto> contextos = contextoRepository.findAll();
        assertThat(contextos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
