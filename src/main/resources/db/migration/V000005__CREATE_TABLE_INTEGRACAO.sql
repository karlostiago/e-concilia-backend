CREATE TABLE `integracao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_integracao` varchar(255) NOT NULL,
  `empresa_id` bigint NOT NULL,
  `operadora_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_integracao` (`operadora_id`,`codigo_integracao`),
  KEY `fk_integracao_empresa_id` (`empresa_id`),
  CONSTRAINT `fk_integracao_empresa_id` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`),
  CONSTRAINT `fk_integracao_operadora_id` FOREIGN KEY (`operadora_id`) REFERENCES `operadora` (`id`)
);