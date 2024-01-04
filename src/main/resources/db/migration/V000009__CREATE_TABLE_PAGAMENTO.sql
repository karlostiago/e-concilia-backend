CREATE TABLE `pagamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bandeira` varchar(255) NOT NULL,
  `codigo_transacao` varchar(255) DEFAULT NULL,
  `data_confirmada_pagamento` date DEFAULT NULL,
  `data_execucao_esperada` date DEFAULT NULL,
  `metodo` varchar(255) NOT NULL,
  `nsu` varchar(255) DEFAULT NULL,
  `numero_cartao` varchar(255) NOT NULL,
  `periodo_id` varchar(255) NULL,
  `proxima_data_execucao` date DEFAULT NULL,
  `responsavel` varchar(255) NOT NULL,
  `status` varchar(255) NULL,
  `tipo` varchar(255) NOT NULL,
  `valor_total` decimal(19,2) NULL,
  PRIMARY KEY (`id`)
);