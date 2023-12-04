/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.TipoDocumentoModel;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

public abstract class TipoDocumentoAction<M extends TipoDocumentoModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	/** Serviz&icirc; del documetno */
	@Autowired protected transient DocumentoService documentoService;
	
	/**
	 * Controlla se la lista dei Tipo Documento sia presente in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 * 
	 * @param tipoFamigliaDocumento  il tipo di famiglia del documento
	 * @param flagSubordinato        se il documento sia subordinato
	 * @param flagRegolarizzazione   se il documento sia di tipo regolarizzazione
	 */
	protected void checkAndObtainListaTipoDocumentoEntrata(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(tipoFamigliaDocumento, flagSubordinato, flagRegolarizzazione);
		RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
		if(!response.hasErrori()) {
				model.setListaTipoDocContabiliaEntrata(response.getElencoTipiDocumento());
				//creo un clone x avere sempre i dati iniziali corretti della lista, per evitare problemi di sovrascrittura dati in sessione
				model.setListaTipoDocContabiliaEntrataClone(ReflectionUtil.deepClone(model.getListaTipoDocContabiliaEntrata()));
		}
		
	}
	
	
	
	/**
	 * Controlla se la lista dei Tipo Documento sia presente in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 * 
	 * @param tipoFamigliaDocumento  il tipo di famiglia del documento
	 * @param flagSubordinato        se il documento sia subordinato
	 * @param flagRegolarizzazione   se il documento sia di tipo regolarizzazione
	 */
	protected void checkAndObtainListaTipoDocumentoSpesa(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(tipoFamigliaDocumento, flagSubordinato, flagRegolarizzazione);
		RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
		if(!response.hasErrori()) {
				model.setListaTipoDocContabiliaSpesa(response.getElencoTipiDocumento());
				//creo un clone x avere sempre i dati iniziali corretti della lista, per evitare problemi di sovrascrittura dati in sessione
				model.setListaTipoDocContabiliaSpesaClone(ReflectionUtil.deepClone(model.getListaTipoDocContabiliaSpesa()));
		}
		
	}
}
