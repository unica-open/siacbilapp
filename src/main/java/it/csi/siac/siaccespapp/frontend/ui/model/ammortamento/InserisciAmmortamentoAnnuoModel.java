/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.ammortamento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class InserisciAmmortamentoAnnuoModel extends GenericBilancioModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -7867697060311383884L;
	
	
	private Integer annoAmmortamentoAnnuo;
	private List<DettaglioAnteprimaAmmortamentoAnnuoCespite> listaDettaglioAnteprimaAmmortamentoAnnuoCespite = new ArrayList<DettaglioAnteprimaAmmortamentoAnnuoCespite>();
	private AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite;
	private DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAntePrimaAmmortamentoAnnuoCespite;
	
	private String codiceContoDettaglio;	
	private int uidDettaglioAnteprima;
	private OperazioneSegnoConto segnoDettaglio;

	/**
	 * Instantiates a new ricerca tipo bene model.
	 */
	public InserisciAmmortamentoAnnuoModel() {
		setTitolo("Ammortamento annuo cespiti");
	}

	/**
	 * @return the annoAmmortamentoAnnuo
	 */
	public Integer getAnnoAmmortamentoAnnuo() {
		return annoAmmortamentoAnnuo;
	}

	/**
	 * @param annoAmmortamentoAnnuo the annoAmmortamentoAnnuo to set
	 */
	public void setAnnoAmmortamentoAnnuo(Integer annoAmmortamentoAnnuo) {
		this.annoAmmortamentoAnnuo = annoAmmortamentoAnnuo;
	}

	/**
	 * @return the anteprimaAmmortamentoAnnuoCespite
	 */
	public AnteprimaAmmortamentoAnnuoCespite getAnteprimaAmmortamentoAnnuoCespite() {
		return anteprimaAmmortamentoAnnuoCespite;
	}

	/**
	 * @param anteprimaAmmortamentoAnnuoCespite the anteprimaAmmortamentoAnnuoCespite to set
	 */
	public void setAnteprimaAmmortamentoAnnuoCespite(AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite) {
		this.anteprimaAmmortamentoAnnuoCespite = anteprimaAmmortamentoAnnuoCespite;
	}

	/**
	 * @return the listaDettaglioAnteprimaAmmortamentoAnnuoCespite
	 */
	public List<DettaglioAnteprimaAmmortamentoAnnuoCespite> getListaDettaglioAnteprimaAmmortamentoAnnuoCespite() {
		return listaDettaglioAnteprimaAmmortamentoAnnuoCespite;
	}

	/**
	 * @param listaDettaglioAnteprimaAmmortamentoAnnuoCespite the listaDettaglioAnteprimaAmmortamentoAnnuoCespite to set
	 */
	public void setListaDettaglioAnteprimaAmmortamentoAnnuoCespite(List<DettaglioAnteprimaAmmortamentoAnnuoCespite> listaDettaglioAnteprimaAmmortamentoAnnuoCespite) {
		this.listaDettaglioAnteprimaAmmortamentoAnnuoCespite = listaDettaglioAnteprimaAmmortamentoAnnuoCespite;
	}

	/**
	 * @return the codiceContoDettaglio
	 */
	public String getCodiceContoDettaglio() {
		return codiceContoDettaglio;
	}

	/**
	 * @param codiceContoDettaglio the codiceContoDettaglio to set
	 */
	public void setCodiceContoDettaglio(String codiceContoDettaglio) {
		this.codiceContoDettaglio = codiceContoDettaglio;
	}
	
	/**
	 * @return the uidDettaglioAnteprima
	 */
	public int getUidDettaglioAnteprima() {
		return uidDettaglioAnteprima;
	}

	/**
	 * @param uidDettaglioAnteprima the uidDettaglioAnteprima to set
	 */
	public void setUidDettaglioAnteprima(int uidDettaglioAnteprima) {
		this.uidDettaglioAnteprima = uidDettaglioAnteprima;
	}
	
	/**
	 * @return the segnoDettaglio
	 */
	public OperazioneSegnoConto getSegnoDettaglio() {
		return segnoDettaglio;
	}

	/**
	 * @param segnoDettaglio the segnoDettaglio to set
	 */
	public void setSegnoDettaglio(OperazioneSegnoConto segnoDettaglio) {
		this.segnoDettaglio = segnoDettaglio;
	}

	/**
	 * @return the dettaglioAntePrimaAmmortamentoAnnuoCespite
	 */
	public DettaglioAnteprimaAmmortamentoAnnuoCespite getDettaglioAntePrimaAmmortamentoAnnuoCespite() {
		return dettaglioAntePrimaAmmortamentoAnnuoCespite;
	}

	/**
	 * @param dettaglioAntePrimaAmmortamentoAnnuoCespite the dettaglioAntePrimaAmmortamentoAnnuoCespite to set
	 */
	public void setDettaglioAntePrimaAmmortamentoAnnuoCespite(
			DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAntePrimaAmmortamentoAnnuoCespite) {
		this.dettaglioAntePrimaAmmortamentoAnnuoCespite = dettaglioAntePrimaAmmortamentoAnnuoCespite;
	}

	/**
	 * Crea request ricerca sintetica dettaglio anteprima ammortamento annuo cespite.
	 *
	 * @return the ricerca sintetica dettaglio anteprima ammortamento annuo cespite
	 */
	public RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite() {
		ModelDetail[] modelDetailsDettagliAnteprima = new ModelDetail[] {DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.Segno, DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.Conto};
		return creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(creaParametriPaginazione(), modelDetailsDettagliAnteprima);
	}
	
	/**
	 * Crea request ricerca sintetica dettaglio anteprima ammortamento annuo cespite.
	 *
	 * @return the ricerca sintetica dettaglio anteprima ammortamento annuo cespite
	 */
	public RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespitePerAnteprima() {
		ModelDetail[] modelDetailsDettagliAnteprima = new ModelDetail[] {DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.AnteprimaAmmortamento};
		return creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(new ParametriPaginazione(0, 1), modelDetailsDettagliAnteprima);
	}

	/**
	 * @param parametriPaginazione
	 * @param modelDetailsDettagliAnteprima
	 * @return
	 */
	private RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(ParametriPaginazione parametriPaginazione, ModelDetail[] modelDetailsDettagliAnteprima) {
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req = creaRequest(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite.class);
		req.setParametriPaginazione(parametriPaginazione);
		AnteprimaAmmortamentoAnnuoCespite anteprima = new AnteprimaAmmortamentoAnnuoCespite();
		anteprima.setAnno(getAnnoAmmortamentoAnnuo());
		req.setModelDetails(modelDetailsDettagliAnteprima);
		req.setAnteprimaAmmortamentoAnnuoCespite(anteprima);
		return req;
	}

	/**
	 * Crea request inserisci anteprima ammortamento annuo cespite.
	 *
	 * @return the inserisci anteprima ammortamento annuo cespite
	 */
	public InserisciAnteprimaAmmortamentoAnnuoCespite creaRequestInserisciAnteprimaAmmortamentoAnnuoCespite() {
		InserisciAnteprimaAmmortamentoAnnuoCespite req = creaRequest(InserisciAnteprimaAmmortamentoAnnuoCespite.class);
		req.setAnno(getAnnoAmmortamentoAnnuo());
		return req;
	}

	/**
	 * Crea request inserisci prime note ammortamento annuo cespite.
	 *
	 * @return the inserisci prime note ammortamento annuo cespite
	 */
	public InserisciPrimeNoteAmmortamentoAnnuoCespite creaRequestInserisciPrimeNoteAmmortamentoAnnuoCespite() {
		InserisciPrimeNoteAmmortamentoAnnuoCespite req = creaRequest(InserisciPrimeNoteAmmortamentoAnnuoCespite.class);
		req.setAnnoAmmortamentoAnnuo(getAnnoAmmortamentoAnnuo());
		return req;
	}
	
	
	
}
