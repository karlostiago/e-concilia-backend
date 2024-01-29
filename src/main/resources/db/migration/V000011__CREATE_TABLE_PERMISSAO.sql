CREATE TABLE `permissao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL UNIQUE,
  PRIMARY KEY (`id`),
  KEY `fk_permissao_usuario_id` (`usuario_id`),
  CONSTRAINT `fk_permissao_usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
);


CREATE TABLE `permissao_funcionalidade` (
  `permissao_id` bigint NOT NULL,
  `funcionalidade` varchar(255) NOT NULL,
  KEY `fk_permissao_funcionalidade_permissao_id` (`permissao_id`),
  CONSTRAINT `fk_permissao_funcionalidade_permissao_id` FOREIGN KEY (`permissao_id`) REFERENCES `permissao` (`id`)
);

ALTER TABLE `permissao_funcionalidade`
ADD CONSTRAINT `un_permissao_funcionalidade_usuario_funcionalidade`
UNIQUE (`permissao_id`, `funcionalidade`);