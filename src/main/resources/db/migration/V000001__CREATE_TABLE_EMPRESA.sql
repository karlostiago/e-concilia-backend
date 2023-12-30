CREATE TABLE `empresa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `razao_social` varchar(100) NOT NULL,
  `nome_fantasia` varchar(100) NOT NULL,
  `cnpj` varchar(14) NOT NULL,
  `logradouro` varchar(100) NOT NULL,
  `numero` varchar(50) NOT NULL,
  `complemento` varchar(255) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `uf` varchar(2) NOT NULL,
  `cep` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `celular` varchar(20) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `ativo` bool DEFAULT FALSE,
  PRIMARY KEY (`id`)
);