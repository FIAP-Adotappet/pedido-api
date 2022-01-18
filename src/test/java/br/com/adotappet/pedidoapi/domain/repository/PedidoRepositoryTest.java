package br.com.adotappet.pedidoapi.domain.repository;

import br.com.adotappet.pedidoapi.domain.entity.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PedidoRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PedidoRepository pedidoRepository;

    private Pedido pedido;

    @BeforeEach
    public void setup() {
        pedido = new Pedido();
    }

    @Test
    public void deveSalvarUmPedidoComDataSolicitacao(){
        pedidoRepository.save(pedido);
        assertNotNull(pedido.getId());
        assertNotNull(pedido.getDataSolicitacao());
    }

    @Test
    public void deveBuscarUmPedido() {
        pedidoRepository.save(pedido);
        Optional<Pedido> pedidoDb = pedidoRepository.findById(pedido.getId());
        assertTrue(pedidoDb.isPresent());
    }

    @Test
    public void deveDeletarUmPedido() {
        pedidoRepository.save(pedido);
        assertFalse(pedidoRepository.findAll().isEmpty());
        pedidoRepository.delete(pedido);
        assertTrue(pedidoRepository.findAll().isEmpty());
    }

    @Test
    public void deveAtualizarUmPedido() {
        pedidoRepository.save(pedido);
        Pedido.Status status = Pedido.Status.FINALIZADO;
        pedido.setStatus(status);
        pedidoRepository.save(pedido);
        Optional<Pedido> pedidoDb = pedidoRepository.findById(pedido.getId());
        assertTrue(pedidoDb.isPresent());
        assertEquals(status, pedidoDb.get().getStatus());
    }
}
