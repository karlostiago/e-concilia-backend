CREATE TABLE `ajuste_venda` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_pagamento_esperada` DATE,
  `data_pedido` DATE NOT NULL,
  `data_pedido_atualizado` DATE,
  `numero_documento` varchar(255) NOT NULL,
  `pedido_cobranca_id` varchar(255) NOT NULL,
  `pedido_id` varchar(255) NOT NULL,
  `periodo_id` varchar(255) NOT NULL,
  `cobranca_id` bigint NOT NULL,
  `valor_ajuste` decimal(19,2) default 0.0,
  PRIMARY KEY (`id`),
  KEY `fk_ajuste_venda_cobranca_id` (`cobranca_id`),
  CONSTRAINT `fk_ajuste_venda_cobranca_id` FOREIGN KEY (`cobranca_id`) REFERENCES `cobranca` (`id`)
);

