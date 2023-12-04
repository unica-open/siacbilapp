/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Classe di model per l'inserimento del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 * 
 */
public class InserisciPreDocumentoSpesaModel extends GenericPreDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7349520623658157136L;
	
	/** Costruttore vuoto di default */
	public InserisciPreDocumentoSpesaModel() {
		setTitolo("Inserimento Predisposizione di Pagamento");
		setNomeAzioneDecentrata(BilConstants.INSERISCI_PREDOCUMENTO_SPESA_DECENTRATO.getConstant());
	}

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
	 * Crea una request per il servizio di {@link InseriscePreDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public InseriscePreDocumentoSpesa creaRequestInseriscePreDocumentoSpesa() {
		InseriscePreDocumentoSpesa request = new InseriscePreDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoSpesa preDocumento = getPreDocumento();
		
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumento.setCausaleSpesa(getCausaleSpesa());
		preDocumento.setContoTesoreria(getContoTesoreria());
		preDocumento.setFlagManuale(Boolean.TRUE);
		
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		preDocumento.setModalitaPagamentoSoggetto(impostaEntitaFacoltativa(getModalitaPagamentoSoggetto()));
		preDocumento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));

		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
		preDocumento.setProvvisorioDiCassa(getProvvisorioCassa());
		request.setPreDocumentoSpesa(preDocumento);
		request.setBilancio(getBilancio());

		return request;
	}
	
}
