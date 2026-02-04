package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.VendedorFilter;
import com.example.SalesHub.dto.request.VendedorReativacaoRequest;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.model.Vendedor;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({VendedorRepositoryImpl.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VendedorRepositoryImplTest {

    @Autowired
    private VendedorRepositoryImpl repositoryCustom;

    @Autowired
    private EntityManager entityManager;

    private Vendedor vendedorAtivo;
    private Vendedor vendedorInativo;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = Usuario.builder()
                .nome("Vendedor Ativo")
                .email("ativo@saleshub.com")
                .senha("123456")
                .ativo(true)
                .build();
        entityManager.persist(usuario);

        vendedorAtivo = Vendedor.builder()
                .usuario(usuario)
                .cpf("11144477735")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .criadoEm(LocalDateTime.now())
                .ativo(true)
                .build();
        entityManager.persist(vendedorAtivo);

        var usuarioInativo = Usuario.builder()
                .nome("Usuario Inativo")
                .email("inativo@saleshub.com")
                .senha("123456")
                .ativo(true)
                .build();
        entityManager.persist(usuarioInativo);

        vendedorInativo = Vendedor.builder()
                .usuario(usuarioInativo)
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .criadoEm(LocalDateTime.now().minusDays(1))
                .ativo(false)
                .build();
        entityManager.persist(vendedorInativo);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deve_buscar_vendedores_paginados_com_projecao_aninhada() {
        var filter = VendedorFilter.builder().build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarVendedores(filter, pageable);

        assertThat(resultado.getContent()).isNotEmpty();
        var projecao = resultado.getContent().getFirst();

        assertThat(projecao.id()).isEqualTo(vendedorAtivo.getId());
        assertThat(projecao.usuario().nome()).isEqualTo("Vendedor Ativo");
        assertThat(projecao.dataNascimento()).isEqualTo(vendedorAtivo.getDataNascimento());
    }

    @Test
    void deve_identificar_vendedor_duplicado_pelo_usuario() {
        var novoVendedorMesmoUsuario = Vendedor.builder()
                .usuario(usuario)
                .build();

        var duplicado = repositoryCustom.buscarVendedorDuplicado(novoVendedorMesmoUsuario);

        assertThat(duplicado).isPresent();
        assertThat(duplicado.get().getId()).isEqualTo(vendedorAtivo.getId());
        assertThat(duplicado.get().getCpf()).isEqualTo("11144477735");
    }

    @Test
    void deve_encontrar_vendedor_para_reativacao_com_dados_corretos() {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(vendedorInativo.getId())
                .cpf("12345678901")
                .build();

        var resultado = repositoryCustom.reativarContaVendedor(request);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getAtivo()).isFalse();
        assertThat(resultado.get().getCpf()).isEqualTo("12345678901");
    }

    @Test
    void nao_deve_encontrar_vendedor_para_reativacao_se_cpf_estiver_errado() {
        var request = VendedorReativacaoRequest.builder()
                .vendedorId(vendedorInativo.getId())
                .cpf("00000000000")
                .build();

        var resultado = repositoryCustom.reativarContaVendedor(request);

        assertThat(resultado).isEmpty();
    }

    @Test
    void deve_buscar_vendedor_existente_somente_se_estiver_ativo() {
        var encontrado = repositoryCustom.buscarVendedorExistente(vendedorAtivo.getId());
        var naoEncontrado = repositoryCustom.buscarVendedorExistente(vendedorInativo.getId());

        assertThat(encontrado).isPresent();
        assertThat(naoEncontrado).isEmpty();
    }
}