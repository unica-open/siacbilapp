/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;


public abstract class BaseMutuoModel extends GenericBilancioModel {
	
	private static final long serialVersionUID = -2347123271682549213L;
	
	protected List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	protected List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	protected List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
	protected List<PeriodoRimborsoMutuo> listaPeriodoMutuo = new ArrayList<PeriodoRimborsoMutuo>();
	protected List<StatoMutuo> listaStatoMutuo = new ArrayList<StatoMutuo>();
	protected List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	protected List<TipoComponenteImportiCapitolo> listaTipoComponente = new ArrayList<TipoComponenteImportiCapitolo>();


	private Mutuo mutuo;

	public List<PeriodoRimborsoMutuo> getListaPeriodoMutuo() {
		return listaPeriodoMutuo;
	}


	public void setListaPeriodoMutuo(List<PeriodoRimborsoMutuo> listaPeriodoMutuo) {
		this.listaPeriodoMutuo = listaPeriodoMutuo;
	}


	public Mutuo getMutuo() {
		return mutuo;
	}


	public void setMutuo(Mutuo mutuo) {
		this.mutuo = mutuo;
	}
	
	
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria;
	}

	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria != null ? listaContoTesoreria : new ArrayList<ContoTesoreria>();
	}

	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}

	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}
	
	public void setMutuoFromResponse(RicercaDettaglioMutuoResponse response) {
		mutuo = response.getMutuo();
	}
	
	protected void addIfNotBlank(List<String> list, String str) {
		if(StringUtils.isNotBlank(str)) {
			list.add(str);
		}
	}
	
	public RicercaDettaglioMutuo creaRequestRicercaDettaglioMutuo() {
		RicercaDettaglioMutuo request = creaRequest(RicercaDettaglioMutuo.class);

		request.setMutuo(getMutuo());
		
		return request;
	}

	public RicercaDettaglioMutuo creaRequestRicercaDettaglioMutuoWithModelDetails(MutuoModelDetail...mutuoModelDetails) {
		RicercaDettaglioMutuo request = creaRequest(RicercaDettaglioMutuo.class);

		request.setMutuo(getMutuo());
		request.setMutuoModelDetails(mutuoModelDetails);
		
		return request;
	}

	public List<StatoMutuo> getListaStatoMutuo() {
		return listaStatoMutuo;
	}


	public void setListaStatoMutuo(List<StatoMutuo> listaStatoMutuo) {
		this.listaStatoMutuo = listaStatoMutuo;
	}
	
	public List<TipoFinanziamento> getListaTipiFinanziamento() {
		return listaTipiFinanziamento;
	}
	
	public void setListaTipiFinanziamento(List<TipoFinanziamento> listaTipiFinanziamento) {
		this.listaTipiFinanziamento = listaTipiFinanziamento != null ? listaTipiFinanziamento : new ArrayList<TipoFinanziamento>();
	}

	public List<TipoComponenteImportiCapitolo> getListaTipoComponente() {
		return listaTipoComponente;
	}

	public void setListaTipoComponente(List<TipoComponenteImportiCapitolo> listaTipoComponente) {
		this.listaTipoComponente = listaTipoComponente;
	}

	public <MER extends BaseMutuoExcelReport> MER creaMutuoExcelReportRequest(Class<MER> cls) {
		MER request = creaRequest(cls);

		request.setEnte(getEnte());
		request.setMutuo(getMutuo());
		request.setXlsx(Boolean.TRUE);

		return request;
	}
}
