/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.Risconto;

/**
 * Classe di model per la gestione di Ratei e Risconti della prima nota integrata. Modulo GEN
 * 
 * @author Valentina
 * @version 1.0.0 - 11/07/2016
 */
public class GestioneRateiERiscontiPrimaNotaIntegrataModel extends GenericBilancioModel {
	
	private static final long serialVersionUID = -4431802812181308017L;
	
	private Rateo rateo;
	private Risconto risconto;
	private List<Risconto> riscontiGiaInseriti = new ArrayList<Risconto>();
	
	private Integer idxRisconto;
	private Integer uidPrimaNota;
	
	private boolean esisteNCDCollegataADocumento;
	private int uidDocumento;

	/**
	 * @return the rateo
	 */
	public Rateo getRateo() {
		return rateo;
	}
	/**
	 * @param rateo the rateo to set
	 */
	public void setRateo(Rateo rateo) {
		this.rateo = rateo;
	}
	/**
	 * @return the risconto
	 */
	public Risconto getRisconto() {
		return risconto;
	}
	/**
	 * @param risconto the risconto to set
	 */
	public void setRisconto(Risconto risconto) {
		this.risconto = risconto;
	}
	/**
	 * @return the riscontiGiaInseriti
	 */
	public List<Risconto> getRiscontiGiaInseriti() {
		return riscontiGiaInseriti;
	}
	/**
	 * @param riscontiGiaInseriti the riscontiGiaInseriti to set
	 */
	public void setRiscontiGiaInseriti(List<Risconto> riscontiGiaInseriti) {
		this.riscontiGiaInseriti = riscontiGiaInseriti;
	}
	/**
	 * @return the idxRisconto
	 */
	public Integer getIdxRisconto() {
		return idxRisconto;
	}
	/**
	 * @param idxRisconto the idxRisconto to set
	 */
	public void setIdxRisconto(Integer idxRisconto) {
		this.idxRisconto = idxRisconto;
	}
	/**
	 * @return the uidPrimaNota
	 */
	public Integer getUidPrimaNota() {
		return uidPrimaNota;
	}
	/**
	 * @param uidPrimaNota the uidPrimaNota to set
	 */
	public void setUidPrimaNota(Integer uidPrimaNota) {
		this.uidPrimaNota = uidPrimaNota;
	}

	/**
	 * @return the esisteNCDCollegataADocumento
	 */
	public boolean isEsisteNCDCollegataADocumento() {
		return esisteNCDCollegataADocumento;
	}
	/**
	 * @param esisteNCDCollegataADocumento the esisteNCDCollegataADocumento to set
	 */
	public void setEsisteNCDCollegataADocumento(boolean esisteNCDCollegataADocumento) {
		this.esisteNCDCollegataADocumento = esisteNCDCollegataADocumento;
	}
	/**
	 * @return the uidDocumento
	 */
	public int getUidDocumento() {
		return uidDocumento;
	}
	/**
	 * @param uidDocumento the uidDocumento to set
	 */
	public void setUidDocumento(int uidDocumento) {
		this.uidDocumento = uidDocumento;
	}
	/**
	 * Crea una request per il servizio di {@link InserisciRateo}.
	 * @return la request creata
	 */
	public InserisciRateo creaRequestInserisciRateo() {
		InserisciRateo request = creaRequest(InserisciRateo.class);
		request.setRateo(getRateo());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNota(){
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		PrimaNota pn = new PrimaNota();
		pn.setUid(uidPrimaNota);
		request.setPrimaNota(pn);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisciRisconto}.
	 * @return la request creata
	 */
	public InserisciRisconto creaRequestInserisciRisconto() {
		InserisciRisconto request = creaRequest(InserisciRisconto.class);
		request.setRisconto(getRisconto());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaRisconto}.
	 * @param riscontoDaAggiornare il risconto da aggiornare
	 * @return la request creata
	 */
	public AggiornaRisconto creaRequestAggiornaRisconto(Risconto riscontoDaAggiornare) {
		AggiornaRisconto request = creaRequest(AggiornaRisconto.class);
		riscontoDaAggiornare.setImporto(getRisconto().getImporto());
		riscontoDaAggiornare.setAnno(getRisconto().getAnno());
		riscontoDaAggiornare.setPrimaNota(getRisconto().getPrimaNota());
		request.setRisconto(riscontoDaAggiornare);
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaRateo}.
	 * @return la request creata
	 */
	public AggiornaRateo creaRequestAggiornaRateo() {
		AggiornaRateo request = creaRequest(AggiornaRateo.class);
		request.setRateo(getRateo());
		return request;
	}
	
	/**
	 * Crea request ricerca dettaglio modulare documento spesa.
	 *
	 * @return the ricerca modulare documento spesa
	 */
	public RicercaModulareDocumentoSpesa creaRequestRicercaDettaglioModulareDocumentoSpesa() {
		RicercaModulareDocumentoSpesa req = creaRequest(RicercaModulareDocumentoSpesa.class);
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(getUidDocumento());
		req.setDocumentoSpesa(documentoSpesa);
		req.setDocumentoSpesaModelDetails(DocumentoSpesaModelDetail.EsisteNCDCollegataADocumento);
		return req;
	}
	
	
}
