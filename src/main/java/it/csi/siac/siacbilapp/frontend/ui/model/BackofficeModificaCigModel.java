/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackoffice;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackoffice.TipoModificaBackofficeCig;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * Classe di modello per il Backoffice delle modifiche CIG
 * @author Martina
 */
public class BackofficeModificaCigModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7315924493227345780L;
	
	private String cig;
	private Impegno impegno;
	private SubImpegno subImpegno;
	private SiopeTipoDebito siopeTipoDebito;
	private SiopeAssenzaMotivazione siopeAssenzaMotivazione;
	private TipoModificaBackofficeCig tipoModifica;
	// Evolutiva BackofficeModificaCig
	private String numeroRemedy;
	
	private List<SiopeTipoDebito> listaSiopeTipoDebito = new ArrayList<SiopeTipoDebito>();
	private List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione = new ArrayList<SiopeAssenzaMotivazione>();
	private List<TipoModificaBackofficeCig> listaTipoModifica = Arrays.asList(TipoModificaBackofficeCig.values());
	

	/** Costruttore vuoto di default */
	public BackofficeModificaCigModel() {
		super();
		setTitolo("Impegni - Backoffice modifica CIG");
	}

	/**
	 * Crea una request per la modifica CIG
	 * @return la request creata
	 */
	public ModificaCigBackoffice creaRequestModificaCigBackoffice() {
		ModificaCigBackoffice request = creaRequest(ModificaCigBackoffice.class);
		Impegno impegnoRequest = null;
		
		if(impegno.getUid()!=0) {
			impegnoRequest = new Impegno();
			impegnoRequest.setUid(impegno.getUid());
		}
		
		if(subImpegno!=null && subImpegno.getUid()!=0) {
			impegnoRequest = new SubImpegno();
			impegnoRequest.setUid(subImpegno.getUid());
		}
		
		impegnoRequest.setSiopeTipoDebito(siopeTipoDebito);
		impegnoRequest.setCig(cig);
		impegnoRequest.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		request.setImpegno(impegnoRequest);
		request.setTipoModifica(tipoModifica);

		// Evolutiva BackofficeModificaCigRemedy
		request.setNumeroRemedy(numeroRemedy);
		
		return request;
	}
		
	/**
	 * @return the tipoModifica
	 */
	public TipoModificaBackofficeCig getTipoModifica() {
		return tipoModifica;
	}
	/**
	 * @param tipoModifica the tipoModifica to set
	 */
	public void setTipoModifica(TipoModificaBackofficeCig tipoModifica) {
		this.tipoModifica = tipoModifica;
	}
	/**
	 * @return the listaTipoModifica
	 */
	public List<TipoModificaBackofficeCig> getListaTipoModifica() {
		return listaTipoModifica;
	}
	/**
	 * @param listaTipoModifica the listaTipoModifica to set
	 */
	public void setListaTipoModifica(List<TipoModificaBackofficeCig> listaTipoModifica) {
		this.listaTipoModifica = listaTipoModifica;
	}
	/**
	 * @return the cig
	 */
	public String getCig() {
		return cig;
	}
	/**
	 * @param cig the cig to set
	 */
	public void setCig(String cig) {
		this.cig = cig;
	}
	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}
	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	/**
	 * @return the subimpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}
	/**
	 * @param subimpegno the subimpegno to set
	 */
	public void setSubImpegno(SubImpegno subimpegno) {
		this.subImpegno = subimpegno;
	}
	/**
	 * @return the siopeTipoDebito
	 */
	public SiopeTipoDebito getSiopeTipoDebito() {
		return siopeTipoDebito;
	}
	/**
	 * @param siopeTipoDebito the siopeTipoDebito to set
	 */
	public void setSiopeTipoDebito(SiopeTipoDebito siopeTipoDebito) {
		this.siopeTipoDebito = siopeTipoDebito;
	}
	/**
	 * @return the siopeAssenzaMotivazione
	 */
	public SiopeAssenzaMotivazione getSiopeAssenzaMotivazione() {
		return siopeAssenzaMotivazione;
	}
	/**
	 * @param siopeAssenzaMotivazione the siopeAssenzaMotivazione to set
	 */
	public void setSiopeAssenzaMotivazione(SiopeAssenzaMotivazione siopeAssenzaMotivazione) {
		this.siopeAssenzaMotivazione = siopeAssenzaMotivazione;
	}
	/**
	 * @return the listaSiopeTipoDebito
	 */
	public List<SiopeTipoDebito> getListaSiopeTipoDebito() {
		return listaSiopeTipoDebito;
	}
	/**
	 * @param listaSiopeTipoDebito the listaSiopeTipoDebito to set
	 */
	public void setListaSiopeTipoDebito(List<SiopeTipoDebito> listaSiopeTipoDebito) {
		this.listaSiopeTipoDebito = listaSiopeTipoDebito != null ? listaSiopeTipoDebito : new ArrayList<SiopeTipoDebito>();
	}
	/**
	 * @return the listaSiopeAssenzaMotivazione
	 */
	public List<SiopeAssenzaMotivazione> getListaSiopeAssenzaMotivazione() {
		return listaSiopeAssenzaMotivazione;
	}
	/**
	 * @param listaSiopeAssenzaMotivazione the listaSiopeAssenzaMotivazione to set
	 */
	public void setListaSiopeAssenzaMotivazione(List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione) {
		this.listaSiopeAssenzaMotivazione = listaSiopeAssenzaMotivazione != null ? listaSiopeAssenzaMotivazione : new ArrayList<SiopeAssenzaMotivazione>();
	}
	/**
	 * @return the numeroRemedy
	 */
	public String getNumeroRemedy() {
		return numeroRemedy;
	}
	/**
	 * @param numeroRemedy the numeroRemedy to set
	 */
	public void setNumeroRemedy(String numeroRemedy) {
		this.numeroRemedy = numeroRemedy;
	}
		
}
