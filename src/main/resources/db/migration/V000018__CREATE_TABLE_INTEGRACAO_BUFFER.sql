CREATE TABLE `integracao_buffer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cnpj` varchar(14) NOT NULL,
  `nome_empresa` VARCHAR(255) NOT NULL,
  `nome_operadora` VARCHAR(255) NOT NULL,
  `data_hora` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);