/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    /**
     * Gestione del change del tipo movimento
     */
    function handleChangeTipoMovimento() {
        var $checked = $('input[name="tipoMovimento"]').filter(':checked');
        var $fields = $('#annoMovimento, #numeroMovimentoGestione, #numeroSubmovimentoGestione');
        var $button = $('#pulsanteCompilazioneGuidataMovimentoGestione');
        if(!$checked.length) {
            $fields.attr('disabled', true);
            $button.addClass('disabled');
            return;
        }
        $fields.removeAttr('disabled');
        $button.removeClass('disabled');
        
        Accertamento.destroy();
        Impegno.destroy();
        if($checked.val() === 'Impegno') {
            Impegno.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined, undefined, undefined, undefined, undefined, '#pulsanteCompilazioneGuidataMovimentoGestione');
        } else if ($checked.val() === 'Accertamento') {
            Accertamento.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined, undefined, undefined, '#pulsanteCompilazioneGuidataMovimentoGestione');
        }
    }

    $(function () {
        $('input[name="tipoMovimento"]').not('[disabled]').change(handleChangeTipoMovimento).change();
    });
}(jQuery);