/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Classe base di model per l'inserimento/ripetizione del PreDocumento di Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/04/2014
 *
 */
public abstract class BaseInserimentoPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 739864148218580944L;

	/**
	 * Restituisce l'uid del PreDocumento, se presente.
	 * 
	 * @return l'uid del preDocumento, se presente; <code>0</code> in caso contrario
	 */
	public Integer getUidPreDocumento() {
		return getPreDocumento() == null ? null : getPreDocumento().getUid();
	}

	/* ***** Requests ***** */
	
	/**
	 * Crea una request per il servizio di {@link InseriscePreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public InseriscePreDocumentoEntrata creaRequestInseriscePreDocumentoEntrata() {
		InseriscePreDocumentoEntrata request = new InseriscePreDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = getPreDocumento();
		
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumento.setCausaleEntrata(getCausaleEntrata());
		preDocumento.setFlagManuale(Boolean.TRUE);
		
		preDocumento.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
     	preDocumento.setProvvisorioDiCassa(getProvvisorioCassa());
     	//CR-4310
     	request.setGestisciModificaImportoAccertamento(getForzaDisponibilitaAccertamento());
		request.setPreDocumentoEntrata(preDocumento);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
}
