CREATE TABLE `ocorrencia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_transacao` DATE,
  `expectativa_data_pagamento` DATE,
  `periodo_id` varchar(255) NOT NULL,
  `transacao_id` varchar(255),
  `valor` decimal(19,2) default 0.0,
  PRIMARY KEY (`id`)
);

