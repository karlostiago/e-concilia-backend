CREATE TABLE `venda` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conciliado` tinyint(1) NOT NULL DEFAULT '0',
  `data_pedido` date NOT NULL,
  `diferenca` decimal(19,2) NOT NULL DEFAULT '0.00',
  `modelo_negocio` varchar(255) NOT NULL,
  `numero_documento` varchar(255) NOT NULL,
  `pedido_id` varchar(255) NOT NULL,
  `periodo_id` varchar(255) NOT NULL,
  `razao_social` varchar(255) NOT NULL,
  `ultima_data_processamento` date DEFAULT NULL,
  `cobranca_id` bigint NOT NULL,
  `pagamento_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_venda_cobranda_id` (`cobranca_id`),
  KEY `fk_venda_pagamento_id` (`pagamento_id`),
  CONSTRAINT `fk_venda_cobranda_id` FOREIGN KEY (`cobranca_id`) REFERENCES `cobranca` (`id`),
  CONSTRAINT `fk_venda_pagamento_id` FOREIGN KEY (`pagamento_id`) REFERENCES `pagamento` (`id`)
);