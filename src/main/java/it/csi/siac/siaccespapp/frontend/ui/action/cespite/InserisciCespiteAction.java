/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.InserisciCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class InserisciTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCespiteAction extends BaseInserisciCespiteAction<InserisciCespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3817734506602097756L;

	private static final String SUCCESS_PRIMA_NOTA = "success_prima_nota";
	
	@Autowired
	private PrimaNotaCespiteService primaNotaCespiteService;
	
	@Override
	public void validateSalva() {
		super.validateSalva();
		//SIAC-6567
		checkCondition(model.getUidMovimentoDettaglio() == null || model.getUidMovimentoDettaglio().intValue() == 0 
				||  model.getImportoMassimoCespite() == null || model.getImportoMassimoCespite().compareTo(model.getCespite().getValoreIniziale()) >=0
						, ErroreBil.IMPORTI_NON_COERENTI.getErrore("L'importo del cespite risulta essere maggiore dell'importo del dettaglio della prima nota a cui lo si vuole collegare."));
	}
	
	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		InserisciCespiteResponse res = inserisciCespite();
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		model.setCespite(res.getCespite());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		if(model.getUidMovimentoDettaglio()== null || model.getUidMovimentoDettaglio().intValue() == 0) {
			return SUCCESS;
		}
		CollegaCespiteRegistroACespiteResponse responseCollega = collegaCespiteMovimentoEP();
		addErrori(responseCollega);
		addMessaggi(responseCollega.getMessaggi());
		setInformazioniMessaggiErroriInSessionePerActionSuccessiva();
		return model.getUidPrimaNota() != null && model.getUidPrimaNota().intValue()!=0? SUCCESS_PRIMA_NOTA: SUCCESS;
	}
	
	@Override
	protected void impostaDatiDefault() {
		model.setCespite(null);
		Integer uidMovimentoEP = sessionHandler.getParametro(BilSessionParameter.UID_MOVIMENTO_DETTAGLIO_DA_COLLEGARE_A_CESPITE);
		if(uidMovimentoEP == null) {
			return;
		}
		Integer uidPrimaNota = sessionHandler.getParametro(BilSessionParameter.UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE);
		BigDecimal importoCespite = sessionHandler.getParametro(BilSessionParameter.IMPORTO_CESPITE_SU_PRIMA_NOTA);
		sessionHandler.setParametro(BilSessionParameter.UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE, null);
		sessionHandler.setParametro(BilSessionParameter.UID_MOVIMENTO_DETTAGLIO_DA_COLLEGARE_A_CESPITE, null);
		
		if(importoCespite != null) {
			model.setCespite(new Cespite());
			model.getCespite().setValoreIniziale(importoCespite);
			model.setImportoMassimoCespite(importoCespite);
		}
		
		model.setUidPrimaNota(uidPrimaNota);
		model.setUidMovimentoDettaglio(uidMovimentoEP);
	}
	
	/**
	 * Collega cespite movimento EP.
	 *
	 * @return the collega cespite registro A (prime note verso inventario contabile) cespite response
	 */
	private CollegaCespiteRegistroACespiteResponse collegaCespiteMovimentoEP() {
		CollegaCespiteRegistroACespite req = model.creaRequestCollegaCespiteRegistroACespite();
		CollegaCespiteRegistroACespiteResponse response = primaNotaCespiteService.collegaCespitePRegistroACespite(req);
		return response;
	}
	
}
