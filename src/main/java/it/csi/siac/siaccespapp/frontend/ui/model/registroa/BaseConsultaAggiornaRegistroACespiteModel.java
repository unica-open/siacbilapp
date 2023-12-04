/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.registroa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite.ElementoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siacfin2ser.model.LiquidazioneModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.ContoModelDetail;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class ConsultaRegistroACespiteModel.
 * @author elisa
 * @version 1.0.0 - 05-11-2018
 */
public abstract class BaseConsultaAggiornaRegistroACespiteModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4959770846022354392L;

	private PrimaNota primaNota;
	
	private Integer uidPrimaNota;
	private MovimentoEP movimentoEP;
	private MovimentoDettaglio movimentoDettaglio;
	
	private Boolean abilitaRicercaCespite = Boolean.FALSE;
	private Boolean abilitaInserimentoCespite = Boolean.FALSE;
	
	private List<ElementoCespite> listaCespitiCollegatiAMovimentoEP = new ArrayList<ElementoCespite>();
	
	//SIAC-7142
	private TipoCausale tipoCausale;
	
	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
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
	 * @return the abilitaRicercaCespite
	 */
	public Boolean isAbilitaRicercaCespite() {
		return abilitaRicercaCespite;
	}

	/**
	 * @param abilitaRicercaCespite the abilitaRicercaCespite to set
	 */
	public void setAbilitaRicercaCespite(Boolean abilitaRicercaCespite) {
		this.abilitaRicercaCespite = abilitaRicercaCespite;
	}

	/**
	 * @return the abilitaInserimentoCespite
	 */
	public Boolean isAbilitaInserimentoCespite() {
		return abilitaInserimentoCespite;
	}

	/**
	 * @param abilitaInserimentoCespite the abilitaInserimentoCespite to set
	 */
	public void setAbilitaInserimentoCespite(Boolean abilitaInserimentoCespite) {
		this.abilitaInserimentoCespite = abilitaInserimentoCespite;
	}
	
	public TipoCausale getTipoCausale() {
		return tipoCausale;
	}

	public void setTipoCausale(TipoCausale tipoCausale) {
		this.tipoCausale = tipoCausale;
	}

	/**
	 * @return the listaCespitiCollegatiAMovimentoEP
	 */
	public List<ElementoCespite> getListaCespitiCollegatiAMovimentoEP() {
		return listaCespitiCollegatiAMovimentoEP;
	}

	/**
	 * @param listaCespitiCollegatiAMovimentoEP the listaCespitiCollegatiAMovimentoEP to set
	 */
	public void setListaCespitiCollegatiAMovimentoEP(List<ElementoCespite> listaCespitiCollegatiAMovimentoEP) {
		this.listaCespitiCollegatiAMovimentoEP = listaCespitiCollegatiAMovimentoEP;
	}

	/**
	 * @return the movimentoEP
	 */
	public MovimentoEP getMovimentoEP() {
		return movimentoEP;
	}

	/**
	 * @param movimentoEP the movimentoEP to set
	 */
	public void setMovimentoEP(MovimentoEP movimentoEP) {
		this.movimentoEP = movimentoEP;
	}
	
	/**
	 * @return the movimentoDettaglio
	 */
	public MovimentoDettaglio getMovimentoDettaglio() {
		return movimentoDettaglio;
	}

	/**
	 * @param movimentoDettaglio the movimentoDettaglio to set
	 */
	public void setMovimentoDettaglio(MovimentoDettaglio movimentoDettaglio) {
		this.movimentoDettaglio = movimentoDettaglio;
	}

	/**
	 * Crea ricerca sintetica movimento EP registro A cespite.
	 *
	 * @return the ricerca sintetica movimento EP registro A cespite
	 */
	public RicercaSinteticaMovimentoEPRegistroACespite creaRicercaSinteticaMovimentoEPRegistroACespite() {
		RicercaSinteticaMovimentoEPRegistroACespite req = creaRequest(RicercaSinteticaMovimentoEPRegistroACespite.class);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setPrimaNota(getPrimaNota());
		req.setModelDetails(new ModelDetailEnum[] {
				MovimentoEPModelDetail.RegistrazioneMovFinModelDetail, MovimentoEPModelDetail.PrimaNotaModelDetail, MovimentoEPModelDetail.MovimentoDettaglioModelDetail, 
				MovimentoEPModelDetail.CausaleEPModelDetail, CausaleEPModelDetail.Evento, CausaleEPModelDetail.Conto,
				MovimentoDettaglioModelDetail.Cespiti, MovimentoDettaglioModelDetail.ContoModelDetail,
				RegistrazioneMovFinModelDetail.EventoMovimento,
				PrimaNotaModelDetail.TipoCausale,
				ContoModelDetail.TipoConto,
				CespiteModelDetail.ImportoSuRegistroA});
		return req;
	}
	
	/**
	 * Crea request ricerca sintetica cespite.
	 *
	 * @return the ricerca sintetica cespite
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite() {
		RicercaSinteticaCespite request = creaRequest(RicercaSinteticaCespite.class);
		request.setMovimentoDettaglio(getMovimentoDettaglio());
		request.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail, CespiteModelDetail.IsInserimentoContestualeRegistroA, CespiteModelDetail.ImportoSuRegistroA);
		request.setParametriPaginazione(creaParametriPaginazione());
		request.getParametriPaginazione().setElementiPerPagina(Integer.MAX_VALUE);
		return request;
	}

	/**
	 * Crea ricerca sintetica movimento dettaglio registro A cespite.
	 *
	 * @return the ricerca sintetica movimento dettaglio registro A cespite
	 */
	public RicercaSinteticaMovimentoDettaglioRegistroACespite creaRicercaSinteticaMovimentoDettaglioRegistroACespite() {
		RicercaSinteticaMovimentoDettaglioRegistroACespite req = creaRequest(RicercaSinteticaMovimentoDettaglioRegistroACespite.class);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setPrimaNota(getPrimaNota());
		req.setModelDetails(new ModelDetailEnum[] {MovimentoEPModelDetail.RegistrazioneMovFinModelDetail, MovimentoEPModelDetail.PrimaNotaModelDetail,
				MovimentoEPModelDetail.CausaleEPModelDetail, CausaleEPModelDetail.Evento, CausaleEPModelDetail.Conto,
				MovimentoDettaglioModelDetail.MovimentoEPModelDetail,
				MovimentoDettaglioModelDetail.Cespiti, MovimentoDettaglioModelDetail.ContoModelDetail,
				MovimentoDettaglioModelDetail.ImportoInventariato,
				RegistrazioneMovFinModelDetail.EventoMovimentoModelDetail,
				SubdocumentoSpesaModelDetail.AttoAmm,
				SubdocumentoSpesaModelDetail.TestataDocumento,
				SubdocumentoEntrataModelDetail.AttoAmm,
				SubdocumentoEntrataModelDetail.TestataDocumento,
				LiquidazioneModelDetail.AttoAmm,
				PrimaNotaModelDetail.TipoCausale,
				ContoModelDetail.TipoConto});
		
		return req;
	}
	
}
