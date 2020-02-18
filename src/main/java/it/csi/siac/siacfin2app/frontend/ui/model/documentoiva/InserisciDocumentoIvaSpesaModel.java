/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Classe di model per l'inserimento del Documento Iva Spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.2 - 07/07/2014
 *
 */
public class InserisciDocumentoIvaSpesaModel extends GenericDocumentoIvaSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5488453680754630799L;
	
	/** Costruttore vuoto di default */
	public InserisciDocumentoIvaSpesaModel() {
		super();
		setTitolo("Inserimento documenti iva spesa");
		setFlagIntracomunitarioUtilizzabile(Boolean.TRUE);
	}
	
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceSubdocumentoIvaSpesa}.
	 * 
	 * @return la request creata
	 */
	public InserisceSubdocumentoIvaSpesa creaRequestInserisceSubdocumentoIvaSpesa() {
		InserisceSubdocumentoIvaSpesa request = creaRequest(InserisceSubdocumentoIvaSpesa.class);
		
		request.setBilancio(getBilancio());
		SubdocumentoIvaSpesa sis = creaSubdocumentoIvaSpesa();
		request.setSubdocumentoIvaSpesa(sis);
		request.setSenzaProtocollo(isRegistrazioneSenzaProtocollo());
		
		if(Boolean.TRUE.equals(getSubdocumentoIva().getFlagIntracomunitario())) {
			impostaSubdocumentoIvaPerIntracomunitario(sis, new SubdocumentoIvaEntrata());
		}
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioFatturaElettronica}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioFatturaElettronica creaRequestRicercaDettaglioFatturaElettronica() {
		RicercaDettaglioFatturaElettronica request = creaRequest(RicercaDettaglioFatturaElettronica.class);
		
		FatturaFEL fatturaFEL = getDocumento().getFatturaFEL();
		request.setFatturaFEL(fatturaFEL);
		
		return request;
	}
	
	/**
	 * Crea il subdocumento Iva spesa.
	 * 
	 * @return il subdocumento creato
	 */
	private SubdocumentoIvaSpesa creaSubdocumentoIvaSpesa() {
		SubdocumentoIvaSpesa sis = getSubdocumentoIva();
		
		sis.setEnte(getEnte());
		sis.setAnnoEsercizio(getAnnoEsercizioInt());
		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sis.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		
		// Imposto documento o subdocumento
		if(Boolean.TRUE.equals(getTipoSubdocumentoIvaQuota())) {
			sis.setSubdocumento(getSubdocumento());
			sis.setDocumento(null);
		} else {
			sis.setDocumento(getDocumento());
			sis.setSubdocumento(null);
		}
		
		// Se non ho lo stato, allora lo creo a PROVVISORIO
		if(sis.getStatoSubdocumentoIva() == null) {
			sis.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		}
		
		TipoRegistroIva tri = getTipoRegistroIva();
		// Imposto il TipoRegistroIva nel Registro IVA (richiesta del servizio)
		sis.getRegistroIva().setTipoRegistroIva(tri);
		
		TipoEsigibilitaIva tei = tri.getTipoEsigibilitaIva();
		if(isRegistrazioneSenzaProtocollo()){
			sis.setDataProtocolloProvvisorio(null);
			sis.setNumeroProtocolloProvvisorio(null);
			sis.setDataProtocolloDefinitivo(null);
			sis.setNumeroProtocolloDefinitivo(null);
		}else if(tei == TipoEsigibilitaIva.IMMEDIATA) {
			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
			sis.setDataProtocolloProvvisorio(null);
			sis.setNumeroProtocolloProvvisorio(null);
		} else if(tei == TipoEsigibilitaIva.DIFFERITA) {
			// Se l'esigibiltà è differita, svuoto il campo del definitivo
			sis.setDataProtocolloDefinitivo(null);
			sis.setNumeroProtocolloDefinitivo(null);
		}
		
		// Intracomunitario
		if(Boolean.TRUE.equals(sis.getFlagIntracomunitario())) {
			// Imposto valuta e importo in valuta
			sis.setValuta(impostaEntitaFacoltativa(getValuta()));
			sis.setImportoInValuta(getImportoInValuta());
		}
		
		return sis;
	}

}
