package br.com.adotappet.pedidoapi.domain.repository;

import br.com.adotappet.pedidoapi.domain.entity.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
