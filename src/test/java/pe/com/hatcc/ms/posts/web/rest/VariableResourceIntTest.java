package pe.com.hatcc.ms.posts.web.rest;

import pe.com.hatcc.ms.posts.MsPostsHatccApp;

import pe.com.hatcc.ms.posts.domain.Variable;
import pe.com.hatcc.ms.posts.repository.VariableRepository;
import pe.com.hatcc.ms.posts.service.VariableService;

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
 * Test class for the VariableResource REST controller.
 *
 * @see VariableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsPostsHatccApp.class)
public class VariableResourceIntTest {

    private static final String DEFAULT_IDCONTEXTO = "AAAAA";
    private static final String UPDATED_IDCONTEXTO = "BBBBB";
    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private VariableRepository variableRepository;

    @Inject
    private VariableService variableService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVariableMockMvc;

    private Variable variable;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VariableResource variableResource = new VariableResource();
        ReflectionTestUtils.setField(variableResource, "variableService", variableService);
        this.restVariableMockMvc = MockMvcBuilders.standaloneSetup(variableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variable createEntity() {
        Variable variable = new Variable()
                .idcontexto(DEFAULT_IDCONTEXTO)
                .nombre(DEFAULT_NOMBRE)
                .descripcion(DEFAULT_DESCRIPCION);
        return variable;
    }

    @Before
    public void initTest() {
        variableRepository.deleteAll();
        variable = createEntity();
    }

    @Test
    public void createVariable() throws Exception {
        int databaseSizeBeforeCreate = variableRepository.findAll().size();

        // Create the Variable

        restVariableMockMvc.perform(post("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variable)))
                .andExpect(status().isCreated());

        // Validate the Variable in the database
        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeCreate + 1);
        Variable testVariable = variables.get(variables.size() - 1);
        assertThat(testVariable.getIdcontexto()).isEqualTo(DEFAULT_IDCONTEXTO);
        assertThat(testVariable.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVariable.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    public void getAllVariables() throws Exception {
        // Initialize the database
        variableRepository.save(variable);

        // Get all the variables
        restVariableMockMvc.perform(get("/api/variables?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(variable.getId())))
                .andExpect(jsonPath("$.[*].idcontexto").value(hasItem(DEFAULT_IDCONTEXTO.toString())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    public void getVariable() throws Exception {
        // Initialize the database
        variableRepository.save(variable);

        // Get the variable
        restVariableMockMvc.perform(get("/api/variables/{id}", variable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(variable.getId()))
            .andExpect(jsonPath("$.idcontexto").value(DEFAULT_IDCONTEXTO.toString()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    public void getNonExistingVariable() throws Exception {
        // Get the variable
        restVariableMockMvc.perform(get("/api/variables/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateVariable() throws Exception {
        // Initialize the database
        variableService.save(variable);

        int databaseSizeBeforeUpdate = variableRepository.findAll().size();

        // Update the variable
        Variable updatedVariable = variableRepository.findOne(variable.getId());
        updatedVariable
                .idcontexto(UPDATED_IDCONTEXTO)
                .nombre(UPDATED_NOMBRE)
                .descripcion(UPDATED_DESCRIPCION);

        restVariableMockMvc.perform(put("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVariable)))
                .andExpect(status().isOk());

        // Validate the Variable in the database
        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeUpdate);
        Variable testVariable = variables.get(variables.size() - 1);
        assertThat(testVariable.getIdcontexto()).isEqualTo(UPDATED_IDCONTEXTO);
        assertThat(testVariable.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVariable.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    public void deleteVariable() throws Exception {
        // Initialize the database
        variableService.save(variable);

        int databaseSizeBeforeDelete = variableRepository.findAll().size();

        // Get the variable
        restVariableMockMvc.perform(delete("/api/variables/{id}", variable.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeDelete - 1);
    }
}
