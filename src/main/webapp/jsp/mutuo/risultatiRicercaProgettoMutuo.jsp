<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
                  <s:form id="formRisultatiRicercaProgettoMutuo" cssClass="form-horizontal" >
                       <s:hidden name="mutuo.uid" />
 		         		
						<h3><span id="id_num_result" class="num_result"></span></h3>
                           <s:hidden name="idx"/>
                     
                           <table class="table tab_left table-hover dataTable" id="tabellaRisultatiRicercaProgettoMutuo" summary="....">
                              <thead>
                                 <tr>
                                	<th scope="col"></th>
                                    <th scope="col">Codice</th>
                                    <th scope="col">Ambito</th>
                                    <th scope="col">Provvedimento</th>
                                    <th scope="col">Valore progetto</th>
	                             </tr>
                              </thead>
                              <tbody>
							  </tbody>
                           </table>
                        
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<s:submit value="progetti" cssClass="btn" action="progettiAssociatiMutuo" />
							<button type="button" disabled="disabled" id="associaProgettiSelezionati" class="btn selezionati">associa progetti selezionati</button>
						</p>  
                  
					</s:form>
				</div>	
			</div>	
		</div>	 
	</div>	

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/risultatiRicercaProgettoMutuo.js"></script>
</body>
</html>