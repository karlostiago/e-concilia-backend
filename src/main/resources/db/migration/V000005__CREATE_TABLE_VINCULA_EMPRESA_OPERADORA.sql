CREATE TABLE `vincula_empresa_operadora` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_integracao` varchar(255) NOT NULL,
  `empresa_id` bigint NOT NULL,
  `operadora_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_vincula_empresa_operadora` (`operadora_id`,`codigo_integracao`),
  KEY `fk_vincula_empresa_operadora_empresa_id` (`empresa_id`),
  CONSTRAINT `fk_vincula_empresa_operadora_empresa_id` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`),
  CONSTRAINT `fk_vincula_empresa_operadora_operadora_id` FOREIGN KEY (`operadora_id`) REFERENCES `operadora` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;