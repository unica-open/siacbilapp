/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.BaseInserisciCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiave;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiaveResponse;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class BaseInserisciCespiteAction.
 * @param <M> la tipizzazione del modello
 */
public abstract class BaseInserisciCespiteAction<M extends BaseInserisciCespiteModel> extends GenericCespiteAction<M> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 7212948999685388083L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaListe(true);
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	
	public String execute() throws Exception {
		impostaDatiDefault();
		return SUCCESS;
	}

	/**
	 * Impostazione dei dati di default
	 */
	protected void impostaDatiDefault() {
		model.setCopiaNumeroInventario(null);
		model.setCespite(null);
	}

	/**
	 * Validate salva.
	 */
	public void validateSalva() {
		Cespite cespite = model.getCespite();
		checkAllCampiCespite(cespite);
	}


	/**
	 * Inserimento del cespite
	 * @return la response del servizio
	 */
	protected InserisciCespiteResponse inserisciCespite() {
		InserisciCespite req = model.creaRequestInserisciCespite();
		InserisciCespiteResponse res = cespiteService.inserisciCespite(req);
		return res;
	}
	
	
	/**
	 * Copia il cespite
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String copia() {
		final String methodName = "copia";
		RicercaCespitePerChiave req = model.creaRequestRicercaCespitePerChiave();
		RicercaCespitePerChiaveResponse res = cespiteService.ricercaCespitePerChiave(req);
		
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(RicercaCespitePerChiave.class, res));
			addErrori(res);
			return INPUT;
		}
		Cespite cespite = res.getCespite();
		log.debug(methodName, "Ottenuto cespite con uid " + cespite.getUid());
		
		// Impostazione dei dati
		impostaCespiteCopiato(cespite);
		
		return SUCCESS;
	}

	/**
	 * Impostazione dei dati del cespite
	 * @param cespite
	 */
	private void impostaCespiteCopiato(Cespite cespite) {
		// Pulizia dei campi
		cespite.setUid(0);
		cespite.setCodice(null);
		cespite.setNumeroInventario(null);
		model.setCespite(cespite);
		
		// Pulisco la ricerca
		model.setCopiaNumeroInventario(null);
	}
	
	/**
	 * Validazione per il metodo {@link #copia()}.
	 */
	public void validateCopia() {
		checkNotNullNorEmpty(model.getCopiaNumeroInventario(), "numero inventario da cui copiare");
	}

}
