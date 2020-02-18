/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Classe di model per l'inserimento del Documento Iva Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/06/2014
 *
 */
public class InserisciDocumentoIvaEntrataModel extends GenericDocumentoIvaEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9033253790494725492L;
	
	/** Costruttore vuoto di default */
	public InserisciDocumentoIvaEntrataModel() {
		super();
		setTitolo("Inserimento documenti iva entrata");
	}
	
	@Override
	public String getTipoSubdocumentoIvaTitolo() {
		return "entrata";
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceSubdocumentoIvaEntrata}.
	 * 
	 * @return la request creata
	 */
	public InserisceSubdocumentoIvaEntrata creaRequestInserisceSubdocumentoIvaEntrata() {
		InserisceSubdocumentoIvaEntrata request = creaRequest(InserisceSubdocumentoIvaEntrata.class);
		
		request.setBilancio(getBilancio());
		request.setSubdocumentoIvaEntrata(creaSubdocumentoIvaEntrata());
		request.setSenzaProtocollo(isRegistrazioneSenzaProtocollo());
		
		return request;
	}
	
	/**
	 * Crea il subdocumento Iva entrata.
	 * 
	 * @return il subdocumento creato
	 */
	private SubdocumentoIvaEntrata creaSubdocumentoIvaEntrata() {
		SubdocumentoIvaEntrata sie = getSubdocumentoIva();
		
		sie.setEnte(getEnte());
		sie.setAnnoEsercizio(getAnnoEsercizioInt());
		sie.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sie.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		
		// Imposto documento o subdocumento
		if(Boolean.TRUE.equals(getTipoSubdocumentoIvaQuota())) {
			sie.setSubdocumento(getSubdocumento());
		} else {
			sie.setDocumento(getDocumento());
		}
		
		// Se non ho lo stato, allora lo creo a PROVVISORIO
		if(sie.getStatoSubdocumentoIva() == null) {
			sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		}
		
		TipoRegistroIva tri = getTipoRegistroIva();
		// Imposto il TipoRegistroIva nel Registro IVA (richiesta del servizio)
		sie.getRegistroIva().setTipoRegistroIva(tri);
		
		TipoEsigibilitaIva tei = tri.getTipoEsigibilitaIva();
		if(isRegistrazioneSenzaProtocollo()){
			sie.setDataProtocolloProvvisorio(null);
			sie.setNumeroProtocolloProvvisorio(null);
			sie.setDataProtocolloDefinitivo(null);
			sie.setNumeroProtocolloDefinitivo(null);
		} else if(tei == TipoEsigibilitaIva.IMMEDIATA) {
			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
			sie.setDataProtocolloProvvisorio(null);
		} else if(tei == TipoEsigibilitaIva.DIFFERITA) {
			// Se l'esigibiltà è differita, svuoto il campo del definitivo
			sie.setDataProtocolloDefinitivo(null);
		}
		
		return sie;
	}

}
