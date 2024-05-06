CREATE TABLE `parametro` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `empresa_id` bigint NOT NULL,
  `operadora_id` bigint NOT NULL,
  `tipo_parametro` varchar(100) NOT NULL,
  `ativo` bool DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `fk_parametro_empresa_id` (`empresa_id`),
  KEY `fk_parametro_operadora_id` (`operadora_id`),
  CONSTRAINT `fk_parametro_empresa_id` FOREIGN KEY (`operadora_id`) REFERENCES `operadora` (`id`),
  CONSTRAINT `fk_parametro_operadora_id` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`)
);