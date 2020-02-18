/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.registroa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class ConsultaRegistroACespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2018
 */
public class AggiornaRegistroACespiteModel extends BaseConsultaAggiornaRegistroACespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5534453435089961411L;
	
	private String codiceEventoCespite;
	private TipoCausale tipoCausale;
	private List<TipoBeneCespite> listaTipoBeneCespite = new ArrayList<TipoBeneCespite>();
	private Integer uidMovimentoDettaglio;
	private Cespite cespite;
	private List<Integer> uidCespitiDaCollegare = new ArrayList<Integer>();
	private boolean disabilitaAzioni = false;
	private Integer uidCespite;
	private BigDecimal importosuRegistroA;
	
	private Integer numeroCespitiCollegatiAMovimento; 
	
	/**
	 * Instantiates a new aggiorna registro a model.
	 */
	public AggiornaRegistroACespiteModel() {
		setTitolo("Integra registro A");
	}

	/**
	 * @return the codiceEventoCespite
	 */
	public String getCodiceEventoCespite() {
		return codiceEventoCespite;
	}

	/**
	 * @param codiceEventoCespite the codiceEventoCespite to set
	 */
	public void setCodiceEventoCespite(String codiceEventoCespite) {
		this.codiceEventoCespite = codiceEventoCespite;
	}

	/**
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale() {
		return tipoCausale;
	}

	/**
	 * @param tipoCausale the tipoCausale to set
	 */
	public void setTipoCausale(TipoCausale tipoCausale) {
		this.tipoCausale = tipoCausale;
	}

	/**
	 * @return the listaTipoBeneCespite
	 */
	public List<TipoBeneCespite> getListaTipoBeneCespite() {
		return listaTipoBeneCespite;
	}

	/**
	 * @param listaTipoBeneCespite the listaTipoBeneCespite to set
	 */
	public void setListaTipoBeneCespite(List<TipoBeneCespite> listaTipoBeneCespite) {
		this.listaTipoBeneCespite = listaTipoBeneCespite;
	}

	
	/**
	 * @return the uidMovimentoEP
	 */
	public Integer getUidMovimentoDettaglio() {
		return uidMovimentoDettaglio;
	}

	/**
	 * Sets the uid movimento dettaglio.
	 *
	 * @param uidMovimentoDettaglio the new uid movimento dettaglio
	 */
	public void setUidMovimentoDettaglio(Integer uidMovimentoDettaglio) {
		this.uidMovimentoDettaglio = uidMovimentoDettaglio;
	}

	/**
	 * @return the cespite
	 */
	public Cespite getCespite() {
		return cespite;
	}

	/**
	 * @param cespite the cespite to set
	 */
	public void setCespite(Cespite cespite) {
		this.cespite = cespite;
	}
	
	/**
	 * @param uidCespitiDaCollegare the uidCespitiDaCollegare to set
	 */
	public void setUidCespitiDaCollegare(List<Integer> uidCespitiDaCollegare) {
		this.uidCespitiDaCollegare = uidCespitiDaCollegare;
	}

	/**
	 * @return the uidCespitiDaCollegare
	 */
	public List<Integer> getUidCespitiDaCollegare() {
		return uidCespitiDaCollegare;
	}
	/**
	 * @return the disabilitaSalvataggio
	 */
	public boolean isDisabilitaAzioni() {
		return disabilitaAzioni;
	}

	/**
	 * Sets the disabilita azioni.
	 *
	 * @param disabilitaAzioni the new disabilita azioni
	 */
	public void setDisabilitaAzioni(boolean disabilitaAzioni) {
		this.disabilitaAzioni = disabilitaAzioni;
	}
	
	/**
	 * @return the uidCespite
	 */
	public Integer getUidCespite() {
		return uidCespite;
	}

	/**
	 * @param uidCespite the uidCespite to set
	 */
	public void setUidCespite(Integer uidCespite) {
		this.uidCespite = uidCespite;
	}

	
	/**
	 * @return the importosuRegistroA
	 */
	public BigDecimal getImportosuRegistroA() {
		return importosuRegistroA;
	}

	/**
	 * @param importosuRegistroA the importosuRegistroA to set
	 */
	public void setImportosuRegistroA(BigDecimal importosuRegistroA) {
		this.importosuRegistroA = importosuRegistroA;
	}

	
	/**
	 * @return the numeroCespitiCollegatiAMovimento
	 */
	public Integer getNumeroCespitiCollegatiAMovimento() {
		return numeroCespitiCollegatiAMovimento;
	}

	/**
	 * @param numeroCespitiCollegatiAMovimento the numeroCespitiCollegatiAMovimento to set
	 */
	public void setNumeroCespitiCollegatiAMovimento(Integer numeroCespitiCollegatiAMovimento) {
		this.numeroCespitiCollegatiAMovimento = numeroCespitiCollegatiAMovimento;
	}

	/**

	 * 
	 * Crea una request per il servizio di {@link RicercaSinteticaTipoBeneCespite}.
	 * @return la request creata
	 */
	public RicercaSinteticaTipoBeneCespite creaRequestRicercaSinteticaTipoBeneCespite() {
		RicercaSinteticaTipoBeneCespite request = creaRequest(RicercaSinteticaTipoBeneCespite.class);
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));		
		return request;
	}
	

	/**
	 * Crea request sollega cespite registro A cespite.
	 *
	 * @return the scollega cespite registro A cespite
	 */
	public ScollegaCespiteRegistroACespite creaRequestSollegaCespiteRegistroACespite() {
		ScollegaCespiteRegistroACespite request = creaRequest(ScollegaCespiteRegistroACespite.class);
		MovimentoDettaglio movimentoDettaglioDaCollegare = new MovimentoDettaglio();
		movimentoDettaglioDaCollegare.setUid(getUidMovimentoDettaglio());
		request.setMovimentoDettaglio(movimentoDettaglioDaCollegare);
		request.setCespite(getCespite());
		return request;
	}

	/**
	 * Crea request aggiorna prima nota cespite da contabilita generale.
	 *
	 * @return the aggiorna prima nota cespite da contabilita generale
	 */
	public AggiornaPrimaNotaSuRegistroACespite creaRequestAggiornaPrimaNotaSuRegistroACespite() {
		AggiornaPrimaNotaSuRegistroACespite request = creaRequest(AggiornaPrimaNotaSuRegistroACespite.class);
		request.setPrimaNota(getPrimaNota());
		return request;
	}
	
	
	/**
	 * Crea request collega cespite registro A cespite.
	 *
	 * @return the collega cespite registro A cespite
	 */
	public CollegaCespiteRegistroACespite creaRequestCollegaCespiteRegistroACespite() {
		CollegaCespiteRegistroACespite request = creaRequest(CollegaCespiteRegistroACespite.class);
		MovimentoDettaglio movimentoDettaglioDaCollegare = new MovimentoDettaglio();
		movimentoDettaglioDaCollegare.setUid(getUidMovimentoDettaglio());
		request.setMovimentoDettaglio(movimentoDettaglioDaCollegare);
		request.setInserimentoContestuale(Boolean.FALSE);
		request.setListaCespiti(creaListaCespiti());
		return request;
	}

	/**
	 * Crea lista cespiti.
	 *
	 * @return the list
	 */
	private List<Cespite> creaListaCespiti() {
		List<Cespite> lista = new ArrayList<Cespite>();
		for (Integer uid : getUidCespitiDaCollegare()) {
			Cespite c = new Cespite();
			c.setUid(uid);
			lista.add(c);
		}
		return lista;
	}

	/**
	 * Crea request aggiorna importo cespite registro A.
	 *
	 * @return the aggiorna importo cespite registro A
	 */
	public AggiornaImportoCespiteRegistroACespite creaRequestAggiornaImportoCespiteRegistroACespite() {
		AggiornaImportoCespiteRegistroACespite request = creaRequest(AggiornaImportoCespiteRegistroACespite.class);
		request.setCespite(getCespite());
		request.setMovimentoDettaglio(getMovimentoDettaglio());
		request.setImportoSuRegistroA(getImportosuRegistroA());
		return request;
	}
	
	
	
	
}
