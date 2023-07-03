CREATE TABLE `taxa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(100) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `entra_em_vigor` date NOT NULL,
  `valido_ate` date NOT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT '0',
  `operadora_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_taxa_operadora_id` (`operadora_id`),
  CONSTRAINT `fk_taxa_operadora_id` FOREIGN KEY (`operadora_id`) REFERENCES `operadora` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;