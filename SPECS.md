# Especificação Técnica: Simulador de Substituição de Páginas

## 1. Visão Geral

Este documento define as especificações para o desenvolvimento de um simulador de algoritmos de substituição de páginas para o gerenciamento de memória virtual. O objetivo é analisar o comportamento de diferentes estratégias de troca de páginas, o impacto do número de molduras (frames) e fenômenos como a Anomalia de Belady.

## 2. Requisitos Funcionais

### 2.1 Algoritmos Suportados

O sistema deve implementar os seguintes algoritmos de substituição de páginas:

- FIFO (First-In, First-Out)
- LRU (Least Recently Used)
- Ótimo (Optimal)
- Segunda Chance
- Clock
- NRU (Not Recently Used)
- LFU (Least Frequently Used)
- MFU (Most Frequently Used)

### 2.2 Entrada de Dados (Input)

O simulador deve aceitar arquivos de trace contendo uma cadeia de referências de páginas.

- **Formato do Arquivo**: Texto plano.
- **Estrutura**: Um número inteiro (ID da página) por linha.
- **Arquivo de Teste Padrão**: `silberschatz2001.trace`.

**Sequência Oficial (Silberschatz et al., 2001):** `7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1`

### 2.3 Métricas de Desempenho

Para cada execução, o software deve calcular e exibir:

- Número total de faltas de página (page faults).
- Taxa de faltas de página (%).
- Número de evicções (páginas removidas da memória).
- Estado final do conjunto residente (conteúdo das molduras).

## 3. Interface do Sistema

### 3.1 Interface de Linha de Comando (CLI)

A execução deve seguir estritamente o seguinte padrão de argumentos:

```bash
./pager --algo <ALGO> --frames <N> --trace <arquivo> [--verbose]
```

- `--algo`: Código ou nome do algoritmo a ser executado.
- `--frames`: Número inteiro de molduras de página disponíveis na memória física.
- `--trace`: Caminho para o arquivo contendo a sequência de acesso.
- `--verbose` (Opcional): Flag para detalhamento da execução.

**Exemplo de uso**:

```bash
./pager --algo lru --frames 3 --trace silberschatz2001.trace
```

### 3.2 Formato de Saída (Output)

A saída no terminal deve seguir o formato abaixo:Algoritmo:

```bash
LRU
Frames: 3
Referências: 20
Faltas de página: 12
Taxa de faltas: 60.00%
Evicções: 12

Conjunto residente final:
frame_ids: 0 1 2
page_ids: 1 7 0
```

## 4 Validação e Casos de Teste

O simulador deve ser validado utilizando o arquivo `silberschatz2001.trace` com configurações de **3** e **4** molduras.

### 4.1 Resultados de Referência (Benchmark)

A implementação deve replicar os seguintes resultados para validação:

| molduras | FIFO      | LRU       | Ótimo    |
| -------- | --------- | --------- | -------- |
| 3        | 15 faltas | 12 faltas | 9 faltas |
| 4        | 10 faltas | 8 faltas  | 8 faltas |

## 5. Diretrizes de Implementação

- **Estrutura de Dados**: Recomenda-se o uso de estruturas que mapeiem informações de controle (ID, tamanho, bits de acesso).

* Documentação:
  - O código deve ser devidamente comentado.
  - Deve ser fornecido um documento auxiliar explicando as decisões de projeto, classes utilizadas e padrões adotados.

## 6. Objetivos de Aprendizado

A implementação deve permitir ao usuário:

1. Compreender a mecânica de substituição de páginas.
2. Analisar o impacto da quantidade de memória física no desempenho (Faltas de Página).
3. Observar experimentalmente a **Anomalia de Belady** (aumento de faltas com o aumento de quadros, típico no FIFO) e o princípio da localidade.
