/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.BackofficeModificaCigModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.BackofficeModificaCigService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackoffice;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackofficeResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * @author Martina
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class BackofficeModificaCigAction extends GenericBilancioAction<BackofficeModificaCigModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 958818799882224510L;

	/** Serviz&icirc; delle codifiche */
	@Autowired protected transient CodificheService codificheService;
	
	/** Servizio per effettuare la modifica Cig */
	@Autowired protected transient BackofficeModificaCigService backofficeModificaCigService;
	
	/**
	 * Carica la lista SiopeTipoDebito e la lista SiopeAssenzaMotivazione
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio 
	 * 
	 */
	private void checkAndObtainListaSIOPE() throws WebServiceInvocationFailureException {
		List<SiopeTipoDebito> listaSiopeTipoDebitoInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_SIOPE_TIPO_DEBITO);
		List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazioneInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_SIOPE_ASSENZA_MOTIVAZIONE);
		
		if(listaSiopeTipoDebitoInSessione == null && listaSiopeAssenzaMotivazioneInSessione == null) {
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(SiopeTipoDebito.class, SiopeAssenzaMotivazione.class);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				addErrori(res);
				throw new WebServiceInvocationFailureException("checkAndObtainListaSIOPE");
			}
			
			listaSiopeTipoDebitoInSessione = res.getCodifiche(SiopeTipoDebito.class);
			listaSiopeAssenzaMotivazioneInSessione = res.getCodifiche(SiopeAssenzaMotivazione.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SIOPE_TIPO_DEBITO, listaSiopeTipoDebitoInSessione);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SIOPE_ASSENZA_MOTIVAZIONE, listaSiopeAssenzaMotivazioneInSessione);
		}
					
		model.setListaSiopeTipoDebito(listaSiopeTipoDebitoInSessione);
		model.setListaSiopeAssenzaMotivazione(listaSiopeAssenzaMotivazioneInSessione);
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		checkAndObtainListaSIOPE();
	}
	
	/**
	 * Validazione per il metodo {@link #modificaCigBackoffice()}
	 */
	public void validateModificaCigBackoffice() {
		
		checkCondition(model.getImpegno() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno/Subimpegno"), true);

		boolean checkUidImpegno = model.getImpegno().getUid() != 0;
		boolean checkUidSubImpegno = model.getSubImpegno() != null && model.getSubImpegno().getUid() != 0;
		boolean checkSiopeAssenzaMotivazione = model.getSiopeAssenzaMotivazione()!=null && model.getSiopeAssenzaMotivazione().getUid()!=0;
		boolean checkCig = StringUtils.isNotBlank(model.getCig());
		
		checkCondition(checkUidImpegno || checkUidSubImpegno, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno/Subimpegno"), true);
		checkCondition(!(checkUidImpegno && checkUidSubImpegno), ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("Sono compilati entrambi gli uid"));
		
		checkCondition(model.getImpegno().getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		checkNotNull(model.getImpegno().getNumero(), "Numero");
		
		checkNotNull(model.getTipoModifica(), "Tipo modifica");
		
		SiopeTipoDebito tipoDebito = ComparatorUtils.searchByUidEventuallyNull(model.getListaSiopeTipoDebito(), model.getSiopeTipoDebito());
		checkNotNullNorInvalidUid(tipoDebito, "Tipo debito SIOPE", true);
		
		if(Constanti.SIOPE_CODE_COMMERCIALE.equals(tipoDebito.getCodice())) {
			// Tipo Debito Commerciale
			if(!checkSiopeAssenzaMotivazione && !checkCig) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("CIG o Motivazione assenza del CIG"));
			} 
			if(checkCig && checkSiopeAssenzaMotivazione) {
				addErrore(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("inserire solo uno dei due campi CIG e Motivazione assenza del CIG"));
			}
		} else {
			// Tipo Debito Non Commerciale
			// siopeAssenzaMotivazione null
			checkCondition(!checkSiopeAssenzaMotivazione, 
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("per il tipo debito NON COMMERCIALE non e' possibile impostare la Motivazione assenza del CIG"));
		}
		
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Logica di richiamo del back end per il salvataggio della modifica del CIG
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String modificaCigBackoffice() {
		ModificaCigBackoffice req = model.creaRequestModificaCigBackoffice();
		ModificaCigBackofficeResponse res = backofficeModificaCigService.modificaCigBackoffice(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		model.setImpegno(null);
		model.setSubImpegno(null);
		model.setSiopeTipoDebito(null);
		model.setCig(null);
		model.setSiopeAssenzaMotivazione(null);
		model.setTipoModifica(null);
		// Evolutiva BackofficeModificaCigRemedy
		model.setNumeroRemedy(null);
		return SUCCESS;
	}

}
