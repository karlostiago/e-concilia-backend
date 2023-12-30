CREATE TABLE `taxa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(100) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `entra_em_vigor` date NOT NULL,
  `valido_ate` date NOT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT '0',
  `contrato_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_taxa_contrato_id` (`contrato_id`),
  CONSTRAINT `fk_taxa_contrato_id` FOREIGN KEY (`contrato_id`) REFERENCES `contrato` (`id`)
);