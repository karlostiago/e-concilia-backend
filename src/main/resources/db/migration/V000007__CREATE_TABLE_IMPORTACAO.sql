CREATE TABLE `importacao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_inicial` date NOT NULL,
  `data_final` date NOT NULL,
  `situacao` varchar(255) NOT NULL,
  `empresa_id` bigint DEFAULT NULL,
  `operadora_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_importacao_empresa_id` (`empresa_id`),
  KEY `fk_importacao_operadora_id` (`operadora_id`),
  CONSTRAINT `fk_importacao_empresa_id` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`),
  CONSTRAINT `fk_importacao_operadora_id` FOREIGN KEY (`operadora_id`) REFERENCES `operadora` (`id`)
);