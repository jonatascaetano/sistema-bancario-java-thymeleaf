document.querySelectorAll('.edit-btn').forEach(button => {
    button.addEventListener('click', function () {
        document.getElementById('clienteId').value = this.getAttribute('data-id');
        document.getElementById('nome').value = this.getAttribute('data-nome');
        document.getElementById('email').value = this.getAttribute('data-email');
        document.getElementById('cpf').value = this.getAttribute('data-cpf');
        document.getElementById('telefone').value = this.getAttribute('data-telefone');
        document.getElementById('endereco').value = this.getAttribute('data-endereco');
        document.getElementById('role').value = this.getAttribute('data-role');
        document.getElementById('numeroConta').value = this.getAttribute('data-numeroconta');
        document.getElementById('agencia').value = this.getAttribute('data-agencia');
        document.getElementById('saldo').value = this.getAttribute('data-saldo');
    });
});

function mascara(i) {

    var v = i.value;

    if (isNaN(v[v.length - 1])) { // impede entrar outro caractere que não seja número
        i.value = v.substring(0, v.length - 1);
        return;
    }

    i.setAttribute("maxlength", "14");
    if (v.length == 3 || v.length == 7) i.value += ".";
    if (v.length == 11) i.value += "-";

}

// Alternar visibilidade da senha
const togglePassword = document.getElementById('togglePassword');
const passwordField = document.getElementById('senha');

togglePassword.addEventListener('click', function () {
    // Alternar tipo do input entre 'password' e 'text'
    const type = passwordField.type === 'password' ? 'text' : 'password';
    passwordField.type = type;

    // Alterar o ícone
    this.classList.toggle('bi-eye');
    this.classList.toggle('bi-eye-slash-fill');
});