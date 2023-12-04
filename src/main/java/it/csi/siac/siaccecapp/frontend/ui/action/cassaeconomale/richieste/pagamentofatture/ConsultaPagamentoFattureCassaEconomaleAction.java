/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamentofatture;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture.ConsultaPagamentoFattureCassaEconomaleModel;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Classe di action per la consultazione del pagamento fatture.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPagamentoFattureCassaEconomaleAction extends BaseConsultaRichiestaEconomaleAction<ConsultaPagamentoFattureCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6018701908341401105L;
	
	@Override
	protected void impostaRichiestaEconomale( RichiestaEconomale richiestaEconomale) {
		super.impostaRichiestaEconomale(richiestaEconomale);
		BigDecimal importo = BigDecimal.ZERO;
		BigDecimal importoSplitReverse = BigDecimal.ZERO;

		model.setListaSubdocumentoSpesa(richiestaEconomale.getSubdocumenti());
		
		for(SubdocumentoSpesa ss : richiestaEconomale.getSubdocumenti()) {
			
			if(ss != null && ss.getDocumento() != null && ss.getDocumento().getUid() != 0) {
				importo = importo.add(ss.getImporto());
				importoSplitReverse=importoSplitReverse.add(ss.getImportoSplitReverse()!=null ? ss.getImportoSplitReverse() : BigDecimal.ZERO);
				if (model.getDocumentoSpesa()==null) {
					model.setDocumentoSpesa(ss.getDocumento());
				}
			}
		}
		model.getDocumentoSpesa().setImporto(importo);
		model.setImportoPagato(model.getRichiestaEconomale().getImporto());
		model.setImportoSplitReverse(importoSplitReverse);
		
	}
	
	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_RICERCA, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_ABILITA};
	}

}
