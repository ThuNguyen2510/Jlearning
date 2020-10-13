$(window).load(function () {
    try {
        if (top !== self) top.location.replace(self.location.href);
    } catch (ex) { console.log('Redirecting error') }
    // if (top !== self && !$.browser.chrome) top.location.replace(self.location.href);

    $('.tp-email-form .button-email').click(function (e) {
        e.preventDefault();
        $('.tp-email-form .email-form-popup').addClass('show');
    });
    $('.tp-email-form .email-form-popup .close-popup').click(function (e) {
        e.preventDefault();
        $('.tp-email-form .email-form-popup').removeClass('show');
        $('.tp-email-form .button-email').addClass('hide');
    });

    $('.tp-email-form .email-form-popup .email-form-subscribe form ').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        form.append('<div class="loading"><i class="fa fa-spinner fa-pulse"></i></div>');
        if (form.find('input[type="email"]').val()) {
            $.ajax({
                type: "POST",
                url: 'https://preview.thimpress.com/m/mailster/subscribe',
                data: form.serialize(),
                complete: function (xhr, textStatus) {
                    form.find('.loading').remove();
                    form.append('<div class="message-success">Please check your inbox or spam folder for confirmation email!</div>');
                    setTimeout(function () {
                        form.find('.message-success').remove();
                    }, 2000);
                }
            });
        } else {
            form.find('.loading').remove();
            form.append('<div class="message-error">Please enter email address</div>');
            setTimeout(function () { form.find('.message-error').remove(); }, 2000);
            form.find('input[type="email"]').addClass('error');
        }


    });

    $('.tp-email-form .email-form-popup .email-form-subscribe form input[type="email"]').focus(function () {
        $(this).removeClass('error');
    });
});