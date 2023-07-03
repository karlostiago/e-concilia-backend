CREATE TABLE `contrato` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativo` tinyint(1) NOT NULL DEFAULT '0',
  `empresa_id` bigint NOT NULL,
  `operadora_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_empresa_id` (`empresa_id`),
  KEY `fk_operadora_id` (`operadora_id`),
  CONSTRAINT `fk_empresa_id` FOREIGN KEY (`operadora_id`) REFERENCES `operadora` (`id`),
  CONSTRAINT `fk_operadora_id` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;