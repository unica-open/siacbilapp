/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.provvedimento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.provvedimento.InserisciProvvedimentoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per l'inserimento di un Provvedimento.
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciProvvedimentoAction extends GenericBilancioAction<InserisciProvvedimentoModel> {
	private static final long serialVersionUID = -5783622456743125980L;
	
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaTipiAtti();
		caricaStatiOperativi();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// controllare la presenza di eventuale logica
		return SUCCESS;
	}
	
	/**
	 * Inserisce il Provvedimento.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserimentoCDU() {
		final String methodName = "inserimento";
		try {
			effettuaInserimento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		sessionHandler.cleanAllSafely();
		return SUCCESS;
	}
	
	/**
	 * Inserisce il Provvedimento.
	 * @return la String corrispondente al risultato della Action
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		try {
			effettuaInserimento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * Effettua l'inserimento del provvedimento
	 * @throws WebServiceInvocationFailureException
	 */
	private void effettuaInserimento() throws WebServiceInvocationFailureException {
		final String methodName = "inserimento";

		// Popolamento request
		InserisceProvvedimento req = model.creaRequestInserisceProvvedimento();
		// richiama servizio 
		InserisceProvvedimentoResponse res = provvedimentoService.inserisceProvvedimento(req);
		
		if (res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(InserisceProvvedimento.class, res));
		}
		model.getAttoAmministrativo().setNumero(res.getAttoAmministrativoInserito().getNumero());
		// Imposto la SAC se necessario
		reimpostaSAC();
		
		log.debug(methodName, "inserimento completato con successo");
		impostaInformazioneSuccesso();
	}
	
	/**
	 * Validazione per il metodo {@link #inserimentoCDU()}.
	 */
	public void validateInserimentoCDU() {
		effettuaValidazione();
	}

	/**
	 * Validazione per il metodo {@link #inserimento()}
	 */
	public void validateInserimento() {
		effettuaValidazione();
	}
	
	/**
	 * Effettua la validazione sul provvedimento
	 */
	private void effettuaValidazione() {
		checkCondition(model.getAttoAmministrativo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Atto Amministrativo"), true);
		checkCondition(model.getAttoAmministrativo().getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		
		TipoAtto tipoAtto = ComparatorUtils.searchByUidEventuallyNull(model.getTipiAtti(), model.getAttoAmministrativo().getTipoAtto());
		checkNotNullNorInvalidUid(tipoAtto, "Tipo Atto");
		model.getAttoAmministrativo().setTipoAtto(tipoAtto);
		
		// Se non e' un movimento interno, allora controllo che il numero sia stato valorizzato
		checkCondition(tipoAtto == null || Boolean.TRUE.equals(tipoAtto.getProgressivoAutomatico()) || model.getAttoAmministrativo().getNumero() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Atto Amministrativo"));
		checkCondition(Math.abs(model.getAttoAmministrativo().getNumero()) < 1000000, ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero Atto Amministrativo", ": non deve avere piu' di 6 cifre"));
	}
	
	/**
	 * Reimpostazione della SAC
	 */
	private void reimpostaSAC() {
		final String methodName = "reimpostaSAC";
		if(model.getAttoAmministrativo() == null || model.getAttoAmministrativo().getStrutturaAmmContabile() == null || model.getAttoAmministrativo().getStrutturaAmmContabile().getUid() == 0) {
			log.debug(methodName, "Non necessario reimpostare la SAC");
		}
		StrutturaAmministrativoContabile sac = model.getAttoAmministrativo().getStrutturaAmmContabile();
		List<StrutturaAmministrativoContabile> listaSAC = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		StrutturaAmministrativoContabile sacSession = ComparatorUtils.searchByUidWithChildren(listaSAC, sac);
		model.getAttoAmministrativo().setStrutturaAmmContabile(sacSession);
	}
	
	/* CARICA LISTE DI CONTROLLO*/
	/**
	 * Carica la lista dei tipi di Atto amministrativo.
	 */
	private void caricaTipiAtti() {
		List<TipoAtto> tipiAtti = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(tipiAtti == null) {
			tipiAtti = new ArrayList<TipoAtto>();
			// Non ho ancora i dati in sessione: li carico
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			logServiceRequest(request);
			
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			if (!response.hasErrori()) {
				tipiAtti = response.getElencoTipi();
				ComparatorUtils.sortByCodice(tipiAtti);
				sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, tipiAtti);
			}
		}
		model.setTipiAtti(tipiAtti);
	}
	
	/**
	 * Crea una lista con gli Stati Operatici relativi agli Atti.
	 */
	private void caricaStatiOperativi() {
		model.setStatiOperativi(Arrays.asList(StatoOperativoAtti.values()));
	}
	
}
