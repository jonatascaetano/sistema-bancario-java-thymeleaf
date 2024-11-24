    document.addEventListener('DOMContentLoaded', function() {
        // Função para verificar se um modal está visível
        function isModalVisible(modalId) {
            var modalElement = document.getElementById(modalId);
            return modalElement.classList.contains('show');
        }

        // Função para limpar mensagens de erro ao fechar modais
        function clearModalError(modalId, errorId) {
            var modalElement = document.getElementById(modalId);
            var errorMessage = document.getElementById(errorId);

            modalElement.addEventListener('hidden.bs.modal', function () {
                if (errorMessage) {
                    errorMessage.textContent = '';
                    errorMessage.classList.add('d-none');
                }
            });
        }

        // Adiciona evento para limpar erros ao fechar modais
        clearModalError('depositModal', 'depositError');
        clearModalError('withdrawModal', 'withdrawError');
        clearModalError('transferModal', 'transferError');

        // Verificar se o withdrawModal está visível
        if (isModalVisible('withdrawModal')) {
            console.log('O modal de retirada está visível.');
        } else {
            console.log('O modal de retirada não está visível.');
        }
    });
