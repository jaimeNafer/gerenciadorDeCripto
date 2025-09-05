package br.com.nafer.gerenciadorcripto.exceptions

class CarteiraValidationException(message: String) : RuntimeException(message)

class CarteiraJaExisteException(nomeCarteira: String) : CarteiraValidationException(
    "Já existe uma carteira com o nome '$nomeCarteira'"
)

class UsuarioJaPossuiCarteiraException(nomeCorretora: String) : CarteiraValidationException(
    "Usuário já possui uma carteira na corretora '$nomeCorretora'"
)